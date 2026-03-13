package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.PaisJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.RolJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pais", description = "Controlador para Obtener los paises registrados en el sistema")
@RestController
@RequestMapping("api/pais")
public class PaisRestController {
    @Autowired
    private PaisJPADAOImplementation paisJPADAOImplementation;

    @Operation(summary = "Obtener todos los países", description = "Retorna la lista de todos los países registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Países obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "204", description = "Lista de Países vacía", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Mensaje de excepción\"}")))
    })

    @GetMapping
    public ResponseEntity GetAll() {
        try {
            Result Result = paisJPADAOImplementation.GetAll();
            if (Result.Correct) {
                if (Result.Objects != null) {
                    if (Result.Objects.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Result);
                    } else {
                        return ResponseEntity.ok(Result);
                    }

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
