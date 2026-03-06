package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.RolJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;


@RestController
@RequestMapping("api/rol")
public class RolRestController {
    @Autowired
    private RolJPADAOImplementation rolJPADAOImplementation;

    @GetMapping
    public ResponseEntity GetAll() {
        try {
            Result Result = rolJPADAOImplementation.GetAll();
            if (Result.Correct) {
                if (Result.Objects != null) {
                    return ResponseEntity.ok(Result.Objects);
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
