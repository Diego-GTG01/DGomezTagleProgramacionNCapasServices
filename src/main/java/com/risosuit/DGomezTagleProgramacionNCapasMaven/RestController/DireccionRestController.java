package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.DireccionJPAImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Direccion;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

@RestController
@RequestMapping("api/direccion")
public class DireccionRestController {
    @Autowired
    private DireccionJPAImplementation direccionJPAImplementation;

    @GetMapping("{idDireccion}")
    public ResponseEntity GetById(@PathVariable("idDireccion") int idDireccion) {
        try {
            Result Result = direccionJPAImplementation.GetById(idDireccion);
            if (Result.Correct) {
                if (Result.Object != null) {
                    return ResponseEntity.ok(Result);
                } else {
                    return ResponseEntity.noContent().build();
                }
            } else {
                return ResponseEntity.badRequest().body(Result.MessageException);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("{idUsuario}")
    public ResponseEntity Add(@PathVariable("idUsuario") int idUsuario, @RequestBody Direccion direccion) {
        try {
            Result Result = direccionJPAImplementation.Add(direccion, idUsuario);
            if (Result.Correct) {
                return ResponseEntity.ok(Result);
            } else {
                return ResponseEntity.badRequest().body(Result.MessageException);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PutMapping("{idUsuario}")
    public ResponseEntity Update(@PathVariable("idUsuario") int idUsuario, @RequestBody Direccion direccion) {
        try {
            try {
                Result Result = direccionJPAImplementation.Update(direccion, idUsuario);
                if (Result.Correct) {
                    return ResponseEntity.ok(Result);

                } else {
                    return ResponseEntity.badRequest().body(Result.MessageException);
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }
    
    @DeleteMapping("{idDireccion}")
    public ResponseEntity Delete(@PathVariable("idDireccion") int idDireccion) {
        try {
            try {
                Result Result = direccionJPAImplementation.Delete(idDireccion);
                if (Result.Correct) {
                    return ResponseEntity.ok(Result);

                } else {
                    return ResponseEntity.badRequest().body(Result.MessageException);
                }
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
