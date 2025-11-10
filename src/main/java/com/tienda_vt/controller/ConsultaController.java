package com.tienda_vt.controller;

import com.tienda_vt.service.CategoriaService;
import com.tienda_vt.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;

    public ConsultaController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var productos = productoService.getProductos(false);
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }

    @PostMapping("/consultaDerivada")
    public String consultaDerivada(@RequestParam() BigDecimal precioInf, @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaDerivada(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam() BigDecimal precioInf, @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam() BigDecimal precioInf, @RequestParam() BigDecimal precioSup,
            Model model) {
        var productos = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", productos);
        model.addAttribute("precioInf", precioInf);
        model.addAttribute("precioSup", precioSup);
        return "/consultas/listado";
    }

    //Tarea 2
    @PostMapping("/consultaAmpliada")
    public String consultaAmpliada(Model model) {
        var productos = productoService.consultaAmpliada();
        model.addAttribute("productos", productos);
        return "/consultas/listado";
    }
}
