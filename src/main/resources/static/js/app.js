// --- Variables Globales ---
let salesChart;
let facturasList = [];
let itemsFactura = []; // Carrito de compras

// --- Inicialización ---
// Esperar a que el HTML cargue para iniciar
document.addEventListener('DOMContentLoaded', function() {
    initDashboard();
});

// --- Lógica del Carrito (La que te interesa) ---

function agregarProductoAFactura() {
    console.log("Ejecutando agregarProductoAFactura...");

    // 1. Obtener valores
    const ref = document.getElementById('facProdRef').value;
    const nombre = document.getElementById('facProdNombre').value;
    const cantInput = document.getElementById('facCant').value;
    const precioInput = document.getElementById('facProdPrecio').value;
    const taxInput = document.getElementById('facProdTax').value;

    const cant = parseInt(cantInput);
    const precio = parseFloat(precioInput);
    const tax = taxInput || "19.00";

    // 2. Validar
    if (!ref || !nombre || isNaN(cant) || cant <= 0 || isNaN(precio)) {
        Swal.fire('Datos incompletos', 'Selecciona un producto y cantidad válida', 'warning');
        return;
    }

    // 3. Agregar o Sumar
    const existente = itemsFactura.find(i => i.code_reference === ref);
    if (existente) {
        existente.quantity += cant;
    } else {
        itemsFactura.push({
            code_reference: ref,
            name: nombre,
            quantity: cant,
            price: precio,
            tax_rate: tax,
            discount_rate: 0,
            unit_measure_id: "70",
            standard_code_id: "1",
            is_excluded: 0,
            tribute_id: "1",
            withholding_taxes: []
        });
    }

    // 4. Renderizar y Limpiar
    renderItemsFactura();
    limpiarInputsProducto();
}

function renderItemsFactura() {
    const tbody = document.getElementById('tablaItemsFactura');
    const totalSpan = document.getElementById('txtTotalFactura');

    tbody.innerHTML = '';
    let total = 0;

    if (itemsFactura.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted p-3">Agrega productos a la factura</td></tr>';
        totalSpan.textContent = '$0';
        return;
    }

    itemsFactura.forEach((item, index) => {
        const subtotal = item.price * item.quantity;
        total += subtotal;
        tbody.innerHTML += `
            <tr>
                <td class="ps-3">${item.name} <small class="text-muted">(${item.code_reference})</small></td>
                <td class="text-center">${item.quantity}</td>
                <td class="text-end">$${item.price.toLocaleString()}</td>
                <td class="text-end fw-bold">$${subtotal.toLocaleString()}</td>
                <td class="text-end pe-3">
                    <button type="button" class="btn btn-sm btn-link text-danger p-0" onclick="eliminarItemFactura(${index})"><i class="fa-solid fa-times"></i></button>
                </td>
            </tr>
        `;
    });

    totalSpan.textContent = '$' + total.toLocaleString();
}

function eliminarItemFactura(index) {
    itemsFactura.splice(index, 1);
    renderItemsFactura();
}

function limpiarInputsProducto() {
    document.getElementById('facProdRef').value = '';
    document.getElementById('facProdNombre').value = '';
    document.getElementById('facCant').value = '1';
    document.getElementById('facProdPrecio').value = '';
    document.getElementById('facProdTax').value = '';
}

// --- Funciones Generales ---

function initDashboard() {
    // Usuario
    const user = JSON.parse(localStorage.getItem('factus_user'));
    if(user) {
        const display = document.getElementById('userNameDisplay');
        if(display) display.textContent = user.nombre || user.username;
    }

    // Stats
    fetch('/api/v1/dashboard/stats')
        .then(r => r.json())
        .then(data => {
            document.getElementById('dashVentas').textContent = '$' + (data.totalVentas || 0).toLocaleString();
            document.getElementById('dashFacturas').textContent = data.totalFacturas || 0;
            document.getElementById('dashClientes').textContent = data.totalClientes || 0;
            document.getElementById('dashProductos').textContent = data.totalProductos || 0;

            const tbody = document.getElementById('recentBillsBody');
            tbody.innerHTML = '';
            if(data.ultimasFacturas && data.ultimasFacturas.length > 0) {
                data.ultimasFacturas.forEach(f => {
                    tbody.innerHTML += `<tr>
                        <td><a href="${f.publicUrl}" target="_blank" class="text-decoration-none fw-bold">${f.number}</a></td>
                        <td>$${f.total.toLocaleString()}</td>
                        <td><span class="badge bg-soft-success text-success">Aprobada</span></td>
                    </tr>`;
                });
            } else {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center text-muted">Sin facturas recientes</td></tr>';
            }

            if(data.ventasPorMes) renderChart(data.ventasPorMes);
        })
        .catch(e => console.error("Error stats", e));

    cargarHistorialFacturas();
    cargarClientes();
    cargarProductos();
    cargarRangos('factura');
    cargarRangos('nota');
    cargarReferencias();
    cargarMunicipios();
}

function logout() {
    Swal.fire({
        title: '¿Cerrar sesión?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Sí, salir'
    }).then((result) => {
        if (result.isConfirmed) {
            localStorage.removeItem('factus_token');
            localStorage.removeItem('factus_user');
            window.location.href = 'login.html';
        }
    });
}

function navTo(id) {
    document.querySelectorAll('.section-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-link').forEach(el => el.classList.remove('active'));
    document.getElementById(id).classList.add('active');
    document.getElementById('nav-' + id).classList.add('active');
    if(id === 'dashboard') initDashboard();
}

// --- Gráficas ---
function renderChart(ventasMes) {
    const ctx = document.getElementById('salesChart').getContext('2d');
    const labels = ventasMes.map(v => `Mes ${v.mes}`);
    const data = ventasMes.map(v => v.total);

    if(salesChart) salesChart.destroy();

    salesChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Ventas ($)',
                data: data,
                backgroundColor: '#2563eb',
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: { y: { beginAtZero: true, grid: { borderDash: [2, 4] } }, x: { grid: { display: false } } }
        }
    });
}

// --- Historial ---
async function cargarHistorialFacturas() {
    try {
        const res = await fetch('/api/v1/facturas');
        if (res.ok) {
            facturasList = await res.json();
            renderFacturas(facturasList);
        }
    } catch (e) {}
}

function renderFacturas(lista) {
    const tbody = document.getElementById('historialFacturasBody');
    tbody.innerHTML = '';
    if (lista.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted p-3">No se encontraron facturas</td></tr>';
        return;
    }

    lista.forEach((f, index) => {
        const clienteNombre = f.cliente ? `${f.cliente.names} ${f.cliente.surname}` : 'Consumidor Final';
        let fechaStr = "Fecha inválida";
        try {
            if (Array.isArray(f.issueDate)) {
                const d = new Date(f.issueDate[0], f.issueDate[1] - 1, f.issueDate[2], f.issueDate[3] || 0, f.issueDate[4] || 0);
                fechaStr = d.toLocaleDateString() + ' ' + d.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            } else {
                const d = new Date(f.issueDate);
                if (!isNaN(d.getTime())) {
                    fechaStr = d.toLocaleDateString() + ' ' + d.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
                }
            }
        } catch(e) {}

        const delay = index * 0.05;
        tbody.innerHTML += `
            <tr class="row-animate" style="animation-delay: ${delay}s">
                <td class="ps-4 fw-bold text-primary">${f.number}</td>
                <td class="small text-muted">${fechaStr}</td>
                <td>${clienteNombre}</td>
                <td class="fw-bold">$${f.total.toLocaleString()}</td>
                <td class="text-end pe-4">
                    <a href="${f.publicUrl}" target="_blank" class="btn btn-action btn-soft-primary" title="Ver PDF"><i class="fa-solid fa-file-pdf"></i></a>
                    <button class="btn btn-action btn-soft-danger" onclick="prepararNotaCredito('${f.number}', '${f.issueDate}', '${f.cufe}', '${f.cliente ? f.cliente.identification : ''}')" title="Crear Nota Crédito"><i class="fa-solid fa-rotate-left"></i></button>
                </td>
            </tr>
        `;
    });
}

function filtrarFacturas() {
    const query = document.getElementById('searchFacturas').value.toLowerCase();
    const filtradas = facturasList.filter(f => {
        const numero = f.number.toLowerCase();
        const cliente = f.cliente ? (f.cliente.names + ' ' + f.cliente.surname).toLowerCase() : '';
        const idCliente = f.cliente ? f.cliente.identification : '';
        return numero.includes(query) || cliente.includes(query) || idCliente.includes(query);
    });
    renderFacturas(filtradas);
}

function prepararNotaCredito(numero, fechaRaw, cufe, clienteId) {
    navTo('nota-credito');
    document.getElementById('ncRefNum').value = numero;
    let fechaISO = "";
    if (Array.isArray(fechaRaw)) {
        const y = fechaRaw[0];
        const m = String(fechaRaw[1]).padStart(2, '0');
        const d = String(fechaRaw[2]).padStart(2, '0');
        fechaISO = `${y}-${m}-${d}`;
    } else {
        fechaISO = String(fechaRaw).split('T')[0];
    }
    document.getElementById('ncRefDate').value = fechaISO;
    document.getElementById('ncRefCufe').value = cufe;
    if(clienteId) {
        document.getElementById('ncCliId').value = clienteId;
        buscarCliente('nc');
    }
    Swal.fire({toast: true, position: 'top-end', icon: 'info', title: 'Datos cargados', timer: 2000});
}

// --- Clientes ---
document.getElementById('clienteForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        identification: document.getElementById('cliId').value,
        names: document.getElementById('cliNombre').value,
        surname: document.getElementById('cliApellido').value,
        email: document.getElementById('cliEmail').value,
        phone: document.getElementById('cliTelefono').value,
        address: document.getElementById('cliDireccion').value,
        municipality_id: document.getElementById('cliMuniId').value,
        dv: "1", legal_organization_id: "2", tribute_id: "21", identification_document_id: "3"
    };
    const isEditing = document.getElementById('btnGuardarCliente').textContent.includes('Actualizar');
    const msg = isEditing ? 'Cliente actualizado' : 'Cliente guardado';
    await guardar('/api/v1/clientes', data, msg);
    if(isEditing) cancelarEdicionCliente();
    else document.getElementById('clienteForm').reset();
    cargarClientes();
});

async function cargarClientes() {
    const res = await fetch('/api/v1/clientes');
    const list = await res.json();
    const tbody = document.getElementById('tablaClientesBody');
    tbody.innerHTML = '';
    list.forEach((c, index) => {
        const delay = index * 0.05;
        tbody.innerHTML += `
        <tr class="row-animate" style="animation-delay: ${delay}s">
            <td class="ps-4 fw-bold text-dark">${c.identification}</td>
            <td>${c.names} ${c.surname}</td>
            <td class="text-muted small">${c.email}</td>
            <td class="text-end pe-4">
                <button class="btn btn-action btn-soft-primary" onclick="usarCliente('${c.identification}')"><i class="fa-solid fa-arrow-right"></i></button>
                <button class="btn btn-action btn-soft-warning" onclick="editarCliente('${c.identification}')"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-action btn-soft-danger" onclick="eliminarCliente('${c.identification}')"><i class="fa-solid fa-trash"></i></button>
            </td>
        </tr>`;
    });
}

function usarCliente(id) { navTo('facturar'); document.getElementById('facCliId').value = id; buscarCliente('fac'); }

async function editarCliente(id) {
    try {
        const res = await fetch(`/api/v1/clientes/${id}`);
        if(res.ok) {
            const c = await res.json();
            document.getElementById('cliId').value = c.identification;
            document.getElementById('cliId').readOnly = true;
            document.getElementById('cliNombre').value = c.names;
            document.getElementById('cliApellido').value = c.surname;
            document.getElementById('cliEmail').value = c.email;
            document.getElementById('cliTelefono').value = c.phone;
            document.getElementById('cliDireccion').value = c.address;
            document.getElementById('cliMuniId').value = c.municipality_id;

            document.getElementById('formClienteTitle').innerHTML = '<i class="fa-solid fa-user-pen me-2"></i> Editar Cliente';
            document.getElementById('btnGuardarCliente').textContent = 'Actualizar Cliente';
            document.getElementById('btnGuardarCliente').classList.replace('btn-primary', 'btn-warning');
            document.getElementById('btnCancelarEdicion').classList.remove('d-none');
            document.getElementById('cardClienteForm').classList.add('editing');
            document.getElementById('cardClienteForm').scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    } catch(e) {}
}

function cancelarEdicionCliente() {
    document.getElementById('clienteForm').reset();
    document.getElementById('cliId').readOnly = false;
    document.getElementById('formClienteTitle').innerHTML = '<i class="fa-solid fa-user-plus me-2"></i> Nuevo Cliente';
    document.getElementById('btnGuardarCliente').textContent = 'Guardar Cliente';
    document.getElementById('btnGuardarCliente').classList.replace('btn-warning', 'btn-primary');
    document.getElementById('btnCancelarEdicion').classList.add('d-none');
    document.getElementById('cardClienteForm').classList.remove('editing');
}

async function eliminarCliente(id) {
    const result = await Swal.fire({
        title: '¿Estás seguro?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef4444',
        confirmButtonText: 'Sí, eliminar'
    });
    if (result.isConfirmed) {
        try {
            const res = await fetch(`/api/v1/clientes/${id}`, { method: 'DELETE' });
            if(res.ok) {
                Swal.fire({icon: 'success', title: 'Eliminado', showConfirmButton: false, timer: 1000});
                cargarClientes();
            }
        } catch(e) {}
    }
}

// --- Productos ---
document.getElementById('productoForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        code_reference: document.getElementById('prodRef').value,
        name: document.getElementById('prodNombre').value,
        price: parseFloat(document.getElementById('prodPrecio').value),
        tax_rate: document.getElementById('prodTax').value,
        unit_measure_id: "70", standard_code_id: "1", is_excluded: 0, tribute_id: "1"
    };
    const isEditing = document.getElementById('btnGuardarProducto').textContent.includes('Actualizar');
    const msg = isEditing ? 'Producto actualizado' : 'Producto guardado';
    await guardar('/api/v1/productos', data, msg);
    if(isEditing) cancelarEdicionProducto();
    else document.getElementById('productoForm').reset();
    cargarProductos();
});

async function cargarProductos() {
    const res = await fetch('/api/v1/productos');
    const list = await res.json();
    const tbody = document.getElementById('tablaProductosBody');
    tbody.innerHTML = '';
    list.forEach((p, index) => {
        const delay = index * 0.05;
        tbody.innerHTML += `
        <tr class="row-animate" style="animation-delay: ${delay}s">
            <td class="ps-4"><span class="badge bg-light text-dark border">${p.code_reference}</span></td>
            <td class="fw-bold text-dark">${p.name}</td>
            <td>$${p.price.toLocaleString()}</td>
            <td><span class="badge bg-soft-primary text-primary">${p.tax_rate}%</span></td>
            <td class="text-end pe-4">
                <button class="btn btn-action btn-soft-primary" onclick="usarProducto('${p.code_reference}')"><i class="fa-solid fa-arrow-right"></i></button>
                <button class="btn btn-action btn-soft-warning" onclick="editarProducto('${p.code_reference}')"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-action btn-soft-danger" onclick="eliminarProducto('${p.code_reference}')"><i class="fa-solid fa-trash"></i></button>
            </td>
        </tr>`;
    });
}

function usarProducto(ref) { navTo('facturar'); document.getElementById('facProdRef').value = ref; buscarProducto('fac'); }

async function editarProducto(ref) {
    try {
        const res = await fetch(`/api/v1/productos/${ref}`);
        if(res.ok) {
            const p = await res.json();
            document.getElementById('prodRef').value = p.code_reference;
            document.getElementById('prodRef').readOnly = true;
            document.getElementById('prodNombre').value = p.name;
            document.getElementById('prodPrecio').value = p.price;
            document.getElementById('prodTax').value = p.tax_rate;

            document.getElementById('formProductoTitle').innerHTML = '<i class="fa-solid fa-box-open me-2"></i> Editar Producto';
            document.getElementById('btnGuardarProducto').textContent = 'Actualizar Producto';
            document.getElementById('btnGuardarProducto').classList.replace('btn-primary', 'btn-warning');
            document.getElementById('btnCancelarEdicionProd').classList.remove('d-none');
            document.getElementById('cardProductoForm').classList.add('editing');
            document.getElementById('cardProductoForm').scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    } catch(e) {}
}

function cancelarEdicionProducto() {
    document.getElementById('productoForm').reset();
    document.getElementById('prodRef').readOnly = false;
    document.getElementById('formProductoTitle').innerHTML = '<i class="fa-solid fa-box me-2"></i> Nuevo Ítem';
    document.getElementById('btnGuardarProducto').textContent = 'Guardar Producto';
    document.getElementById('btnGuardarProducto').classList.replace('btn-warning', 'btn-primary');
    document.getElementById('btnCancelarEdicionProd').classList.add('d-none');
    document.getElementById('cardProductoForm').classList.remove('editing');
}

async function eliminarProducto(ref) {
    const result = await Swal.fire({
        title: '¿Estás seguro?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef4444',
        confirmButtonText: 'Sí, eliminar'
    });
    if (result.isConfirmed) {
        try {
            const res = await fetch(`/api/v1/productos/${ref}`, { method: 'DELETE' });
            if(res.ok) {
                Swal.fire({icon: 'success', title: 'Eliminado', showConfirmButton: false, timer: 1000});
                cargarProductos();
            }
        } catch(e) {}
    }
}

// --- Búsquedas ---
async function buscarCliente(prefix) {
    const id = document.getElementById(`${prefix}CliId`).value;
    if(!id) return;
    try {
        const res = await fetch(`/api/v1/clientes/${id}`);
        if(res.ok) {
            const c = await res.json();
            document.getElementById(`${prefix}CliNombre`).value = `${c.names} ${c.surname}`;
            document.getElementById(`${prefix}CliEmail`).value = c.email;
            document.getElementById(`${prefix}CliDireccion`).value = c.address;
            document.getElementById(`${prefix}CliTel`).value = c.phone;
            document.getElementById(`${prefix}CliNames`).value = c.names;
            document.getElementById(`${prefix}CliSurname`).value = c.surname;
            document.getElementById(`${prefix}CliMuni`).value = c.municipality_id;
            Swal.fire({toast: true, position: 'top-end', icon: 'success', title: 'Cliente cargado', timer: 1000, showConfirmButton: false});
        } else Swal.fire('No encontrado', 'Cliente no existe', 'warning');
    } catch(e) {}
}

async function buscarProducto(prefix) {
    const ref = document.getElementById(`${prefix}ProdRef`).value;
    if(!ref) return;
    try {
        const res = await fetch(`/api/v1/productos/${ref}`);
        if(res.ok) {
            const p = await res.json();
            document.getElementById(`${prefix}ProdNombre`).value = p.name;
            document.getElementById(`${prefix}ProdPrecio`).value = p.price;
            document.getElementById(`${prefix}ProdTax`).value = p.tax_rate;
            if(prefix === 'fac') {
                Swal.fire({toast: true, position: 'top-end', icon: 'success', title: 'Producto encontrado', timer: 1000, showConfirmButton: false});
            } else {
                Swal.fire({toast: true, position: 'top-end', icon: 'success', title: 'Producto cargado', timer: 1000, showConfirmButton: false});
            }
        } else Swal.fire('No encontrado', 'Producto no existe', 'warning');
    } catch(e) {}
}

// --- Envíos ---
document.getElementById('facturaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    enviarDocumento('/api/v1/facturas/validar', 'factura', 'btnFacturar');
});

document.getElementById('notaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    enviarDocumento('/api/v1/notas-credito/validar', 'nota', 'btnNota');
});

async function enviarDocumento(url, tipo, btnId) {
    const prefix = tipo === 'factura' ? 'fac' : 'nc';
    const btn = document.getElementById(btnId);
    const rangoId = document.getElementById(tipo === 'factura' ? 'rangoFactura' : 'rangoNota').value;

    if(!rangoId) { Swal.fire('Error', 'Selecciona un rango', 'error'); return; }
    if(!document.getElementById(`${prefix}CliNombre`).value) { Swal.fire('Faltan datos', 'Selecciona un cliente', 'warning'); return; }

    if (tipo === 'factura' && itemsFactura.length === 0) {
        Swal.fire('Carrito vacío', 'Agrega al menos un producto a la factura', 'warning');
        return;
    }

    btn.disabled = true;
    btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Procesando...';

    const payload = {
        numbering_range_id: rangoId,
        reference_code: (tipo === 'factura' ? "FAC-" : "NC-") + Date.now(),
        observation: tipo === 'factura' ? document.getElementById('facObs').value : "Generado desde Factus Pro",
        payment_form: tipo === 'factura' ? document.getElementById('facFormaPago').value : "1",
        payment_due_date: tipo === 'factura' ? document.getElementById('facFechaVence').value : "2024-12-31",
        payment_method_code: tipo === 'factura' ? document.getElementById('facMedioPago').value : "10",
        customer: {
            identification: document.getElementById(`${prefix}CliId`).value,
            dv: "1",
            names: document.getElementById(`${prefix}CliNames`).value,
            surname: document.getElementById(`${prefix}CliSurname`).value,
            email: document.getElementById(`${prefix}CliEmail`).value,
            phone: document.getElementById(`${prefix}CliTel`).value,
            address: document.getElementById(`${prefix}CliDireccion`).value,
            legal_organization_id: "2", tribute_id: "21", identification_document_id: "3",
            municipality_id: document.getElementById(`${prefix}CliMuni`).value || "980"
        },
        items: tipo === 'factura' ? itemsFactura : [{
            code_reference: document.getElementById(`${prefix}ProdRef`).value,
            name: document.getElementById(`${prefix}ProdNombre`).value,
            quantity: parseInt(document.getElementById(`${prefix}Cant`).value),
            price: parseFloat(document.getElementById(`${prefix}ProdPrecio`).value),
            tax_rate: document.getElementById(`${prefix}ProdTax`).value,
            discount_rate: 0, unit_measure_id: "70", standard_code_id: "1", is_excluded: 0, tribute_id: "1", withholding_taxes: []
        }]
    };

    if(tipo === 'nota') {
        payload.bill_id = document.getElementById('ncBillId').value;
        payload.correction_concept_code = document.getElementById('ncConcepto').value;
        payload.billing_reference = {
            number: document.getElementById('ncRefNum').value,
            issue_date: document.getElementById('ncRefDate').value,
            scheme_name: "CUFE-SHA384",
            scheme_id: document.getElementById('ncRefCufe').value
        };
    }

    try {
        const res = await fetch(url, { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(payload) });
        const data = await res.json();
        if(res.ok) {
            Swal.fire({title: '¡Aprobado!', html: `Documento: <strong>${data.data.bill.number}</strong>`, icon: 'success', confirmButtonText: 'Ver PDF'}).then((r) => { if(r.isConfirmed) window.open(data.data.bill.public_url, '_blank'); });
            if(tipo === 'factura') {
                initDashboard();
                itemsFactura = [];
                renderItemsFactura();
                document.getElementById('facturaForm').reset();
            }
        } else Swal.fire('Rechazado', data.message || 'Error', 'error');
    } catch(e) { Swal.fire('Error', 'Fallo de conexión', 'error'); }
    finally { btn.disabled = false; btn.innerHTML = 'Enviar'; }
}

async function guardar(url, data, msg) {
    try {
        const res = await fetch(url, { method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(data) });
        if(res.ok) { Swal.fire({icon: 'success', title: 'Guardado', text: msg, timer: 1500, showConfirmButton: false}); initDashboard(); }
        else Swal.fire('Error', 'No se pudo guardar', 'error');
    } catch(e) { Swal.fire('Error', 'Fallo de conexión', 'error'); }
}

async function cargarRangos(tipo) {
    const select = document.getElementById(tipo === 'factura' ? 'rangoFactura' : 'rangoNota');
    try {
        const res = await fetch('/api/v1/rangos');
        const rangos = await res.json();
        select.innerHTML = '';
        const filtrados = rangos.filter(r => {
            const doc = r.document.toLowerCase();
            if(tipo === 'factura') return doc.includes('factura');
            if(tipo === 'nota') return doc.includes('nota crédito') || r.prefix === 'NC';
            return false;
        });
        filtrados.forEach(r => {
            const option = document.createElement('option');
            option.value = r.id;
            option.text = `${r.document} - ${r.prefix} (${r.from}-${r.to})`;
            select.appendChild(option);
        });
    } catch(e) {}
}

async function cargarReferencias() {
    const selForma = document.getElementById('facFormaPago');
    const selMedio = document.getElementById('facMedioPago');
    if(selForma.options.length > 0) return;

    const addOpt = (sel, val, txt) => { const o = document.createElement('option'); o.value = val; o.text = txt; sel.appendChild(o); };
    addOpt(selForma, "1", "Contado"); addOpt(selForma, "2", "Crédito");
    addOpt(selMedio, "10", "Efectivo"); addOpt(selMedio, "42", "Consignación"); addOpt(selMedio, "48", "Tarjeta Crédito");

    try {
        const resF = await fetch('/api/v1/referencias/formas-pago');
        if(resF.ok) {
            const formas = await resF.json();
            if(formas.length > 0) {
                selForma.innerHTML = '';
                formas.forEach(f => addOpt(selForma, f.code, f.name));
            }
        }
        const resM = await fetch('/api/v1/referencias/medios-pago');
        if(resM.ok) {
            const medios = await resM.json();
            if(medios.length > 0) {
                selMedio.innerHTML = '';
                medios.forEach(m => addOpt(selMedio, m.code, m.name));
            }
        }
    } catch(e) {}

    selForma.value = "1"; selMedio.value = "10"; toggleVencimiento();
}

function toggleVencimiento() {
    const div = document.getElementById('divVencimiento');
    if(document.getElementById('facFormaPago').value === "2") div.style.display = 'block';
    else { div.style.display = 'none'; document.getElementById('facFechaVence').valueAsDate = new Date(); }
}

async function cargarMunicipios() {
    const select = document.getElementById('cliMuniId');
    if (select.options.length > 1) return;
    try {
        const res = await fetch('/api/v1/municipios?nombre=Neiva');
        const municipios = await res.json();
        select.innerHTML = '<option value="" selected disabled>Seleccione un municipio...</option>';
        municipios.forEach(m => {
            const option = document.createElement('option');
            option.value = m.id;
            option.text = `${m.name} (${m.department})`;
            select.appendChild(option);
        });
    } catch(e) {
        console.error("Error cargando municipios", e);
        select.innerHTML = '<option value="" disabled>Error cargando lista</option>';
    }
}