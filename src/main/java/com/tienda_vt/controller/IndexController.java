package com.tienda_vt.controller;

import com.tienda_vt.service.CategoriaService;
import com.tienda_vt.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        return "/index";
    }

    @GetMapping("/consultas/{idCategoria}")

    public String listado(@PathVariable("idCategoria") Integer idCategoria, Model model) {
        var categoriaOpt = categoriaService.getCategoria(idCategoria);
        if (categoriaOpt.isEmpty()) {
            ///No encontró la categoría
            model.addAttribute("productos", java.util.Collections.EMPTY_LIST);
        } else {
            var categoria = categoriaOpt.get();
            model.addAttribute("productos", categoria.getProductos());
        }
        return "/index";
    }
}
