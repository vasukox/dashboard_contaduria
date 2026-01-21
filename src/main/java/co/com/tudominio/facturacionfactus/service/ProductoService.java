package co.com.tudominio.facturacionfactus.service;

import co.com.tudominio.facturacionfactus.model.dto.factura.BillItem;
import co.com.tudominio.facturacionfactus.model.entity.Producto;
import co.com.tudominio.facturacionfactus.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorCodigo(String codigo) {
        return productoRepository.findByCodeReference(codigo)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado: {}", codigo);
                    return new RuntimeException("Producto no encontrado: " + codigo);
                });
    }

    public Producto guardarProducto(Producto producto) {
        log.info("Guardando producto: {} - {}", producto.getCodeReference(), producto.getName());
        Optional<Producto> existente = productoRepository.findByCodeReference(producto.getCodeReference());
        if (existente.isPresent()) {
            log.debug("Producto existente, actualizando...");
            producto.setId(existente.get().getId());
        }
        return productoRepository.save(producto);
    }

    public void eliminarProducto(String codigo) {
        log.info("Eliminando producto: {}", codigo);
        Producto producto = buscarPorCodigo(codigo);
        productoRepository.delete(producto);
    }

    // Método utilitario para convertir de Entidad a DTO de Factura
    public BillItem convertirABillItem(Producto producto, Integer cantidad) {
        return BillItem.builder()
                .codeReference(producto.getCodeReference())
                .name(producto.getName())
                .quantity(cantidad)
                .price(producto.getPrice())
                .taxRate(producto.getTaxRate())
                .discountRate(0.0) // Por defecto sin descuento
                .unitMeasureId(producto.getUnitMeasureId())
                .standardCodeId(producto.getStandardCodeId())
                .isExcluded(producto.getIsExcluded())
                .tributeId(producto.getTributeId())
                .withholdingTaxes(Collections.emptyList())
                .build();
    }
}