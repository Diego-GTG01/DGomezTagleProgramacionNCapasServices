package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.ColoniaJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

@RestController
@RequestMapping("api/colonia")
public class ColoniaRestController {
    @Autowired
    private ColoniaJPADAOImplementation coloniaJPADAOImplementation;

    @GetMapping
    public ResponseEntity GetMunicipioByEstado(@RequestParam("idMunicipio") int idMunicipio) {
        try {
            Result Result = coloniaJPADAOImplementation.getColoniaByMunicipio(idMunicipio);
            if (Result.Correct) {
                if (Result.Objects != null || !Result.Objects.isEmpty()) {
                    return ResponseEntity.ok(Result);
                } else {
                    return ResponseEntity.noContent().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

    }

}
