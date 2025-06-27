package com.ecommerce.micrommerce.web.controller;

import com.ecommerce.micrommerce.web.dao.ProductDao;
import com.ecommerce.micrommerce.web.exceptions.ProductValidator;
import com.ecommerce.micrommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.micrommerce.web.exceptions.ProduitIntrouvableException;
import com.ecommerce.micrommerce.web.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.validation.BeanPropertyBindingResult;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api( "API pour les opérations CRUD sur les produits.")
@RestController
public class ProductController {

    @Autowired
    private ProductValidator productValidator;

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    private void validateProduct(Product product) {
        // Créer un BindingResult temporaire
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(product, "product");
        productValidator.validate(product, bindingResult);

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            throw new ProduitGratuitException(message);
        }
    }

    @DeleteMapping (value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDao.deleteById(id);
    }

    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {
        validateProduct(product);
        productDao.save(product);
    }

    //Récupérer la liste des produits
    @GetMapping("/Produits")
    public List<Product> listeProduits() {
        return productDao.findAll();
    }

    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);
        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");
        return produit;
    }

    @GetMapping(value = "test/produits/{prixLimit}")
    public List<Product> testeDeRequetes(@PathVariable int prixLimit) {
        return productDao.findByPrixGreaterThan(prixLimit);
    }

    @GetMapping(value = "/AdminProduits")
    public Map<String, Double> calculerMargeProduit() {
        List<Product> produits = productDao.findAll();
        Map<String, Double> marges = new HashMap<>();

        for (Product produit : produits) {
            double marge = produit.getPrix() - produit.getPrixAchat();
            marges.put(produit.toString(), marge);
        }
        return marges;
    }

    @GetMapping("/ProduitsTri")
    public List<Product> trierProduitsParOrdreAlphabetique() {
        return productDao.findAllByOrderByNomAsc();
    }

    @PostMapping(value = "/Produits")
    public ResponseEntity<Product> ajouterProduit(@RequestBody Product product) {
        validateProduct(product);
        Product productAdded = productDao.save(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
}