package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.UsuarioJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Usuario;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.Service.ValidationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;
    // Validacion de datos
    @Autowired
    private ValidationService validationservice;

    @GetMapping
    public ResponseEntity GetAll() {
        try {
            Result Result = usuarioJPADAOImplementation.GetAll();
            if (Result.Correct) {
                if (Result.Objects != null || !Result.Objects.isEmpty()) {
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

    @GetMapping("{idUsuario}")
    public ResponseEntity GetById(@PathVariable("idUsuario") int idUsuario) {
        try {
            Result Result = usuarioJPADAOImplementation.GetById(idUsuario);
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

    @PostMapping("busqueda")
    public ResponseEntity Busqueda(@RequestBody Usuario usuario) {

        try {
            Result Result = usuarioJPADAOImplementation.Busqueda(usuario);
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

    }

    @PostMapping
    public ResponseEntity Add(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        } else {

            try {
                Result Result = usuarioJPADAOImplementation.Add(usuario);
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

        }

    }

    @PutMapping
    public ResponseEntity Update(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        } else {

            try {
                Result Result = usuarioJPADAOImplementation.Update(usuario);
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

        }

    }

    @DeleteMapping("{idUsuario}")
    public ResponseEntity Delete(@PathVariable("idUsuario") int idUsuario) {

        try {
            Result Result = usuarioJPADAOImplementation.Delete(idUsuario);
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

    }

    @PatchMapping("imagen")
    public ResponseEntity UpdateImagen(@RequestBody Usuario usuario) {
        try {
            Result Result = usuarioJPADAOImplementation.UpdateImagen(usuario);
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
    }

    @PatchMapping("status/{idUsuario}")
    public ResponseEntity UpdateActivo(@PathVariable("idUsuario") int idUsuario) {
        try {
            Result Result = usuarioJPADAOImplementation.UpdateActivo(idUsuario);
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

    }

}
