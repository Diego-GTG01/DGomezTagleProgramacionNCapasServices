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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Colonia", description = "Controlador para Obtener las colonias registradas en el sistema")
@RestController
@RequestMapping("api/colonia")
public class ColoniaRestController {
    @Autowired
    private ColoniaJPADAOImplementation coloniaJPADAOImplementation;

    @Operation(summary = "Obtener todas las colonias Segun el ID Municipio", description = "Retorna la lista de todas las colonias registradas en el sistema Segun el ID Municipio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Colonias obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "204", description = "Lista de Colonias vacía", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Result.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"error\": \"Mensaje de excepción\"}")))
    })

    @GetMapping
    public ResponseEntity GetColoniaByMunicipio(@RequestParam("idMunicipio") int idMunicipio) {
        try {
            Result Result = coloniaJPADAOImplementation.getColoniaByMunicipio(idMunicipio);
            if (Result.Correct) {
                if (Result.Objects != null || !Result.Objects.isEmpty()) {
                    return ResponseEntity.ok(Result);
                } else {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Result);
                    
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
