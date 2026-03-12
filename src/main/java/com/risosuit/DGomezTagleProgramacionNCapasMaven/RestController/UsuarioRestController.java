package com.risosuit.DGomezTagleProgramacionNCapasMaven.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO.UsuarioJPADAOImplementation;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Direccion;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.ErroresArchivo;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Usuario;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.Service.ValidationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/usuario")
public class UsuarioRestController {
    @Autowired
    private UsuarioJPADAOImplementation usuarioJPADAOImplementation;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> Add(
            @Valid @RequestPart("usuario") Usuario usuario,
            BindingResult bindingResult,
            @RequestPart(name = "imagen", required = false) MultipartFile imagen, Model model) {
        Result result = null;

        if (bindingResult.hasErrors()) {
            result = new Result();
            result.Correct = false;
            result.MessageException = "Error de validación";

            Map<String, String> campoErrores = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                String field = error.getField().replace('.', '_').replace("[0]", "");
                String mensaje = error.getDefaultMessage();

                if (campoErrores.containsKey(field)) {
                    String prev = campoErrores.get(field);
                    campoErrores.put(field, prev + "\n " + mensaje);
                } else {
                    campoErrores.put(field, mensaje);
                }
            }

            result.Object = campoErrores;
            return ResponseEntity.badRequest().body(result);
        }

        try {
            String nombreArchivo = imagen.getOriginalFilename();
            String[] cadena = nombreArchivo.split("\\.");

            if (cadena.length > 1
                    && (cadena[1].equals("jpg") || cadena[1].equals("png"))) {
                if (imagen != null && !imagen.isEmpty()) {
                    byte[] bytes = imagen.getBytes();
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    usuario.setImagenFile(base64);

                }
            } else {
                result = new Result();
                result.Correct = false;
                result.MessageException = "Archivo no permitido, solo se permiten imagenes jpg y png";
                result.Object = usuario;
                return ResponseEntity.badRequest().body(result);
            }

            result = new Result();
            result = usuarioJPADAOImplementation.Add(usuario);

            if (result.Correct) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity Update(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
        Result Result = null;
        if (bindingResult.hasErrors()) {
            Result = new Result();
            Result.Correct = false;
            Result.MessageException = "Error de validación";

            Map<String, String> campoErrores = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                String field = error.getField().replace('.', '_').replace("[0]", "");
                String mensaje = error.getDefaultMessage();

                if (campoErrores.containsKey(field)) {
                    String prev = campoErrores.get(field);
                    campoErrores.put(field, prev + "\n " + mensaje);
                } else {
                    campoErrores.put(field, mensaje);
                }
            }

            Result.Object = campoErrores;
            return ResponseEntity.badRequest().body(Result);
        } else {

            try {
                Result = new Result();
                Result = usuarioJPADAOImplementation.Update(usuario);
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
                return ResponseEntity.badRequest().body(Result);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

    }

    @PatchMapping(value = "imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity UpdateImagen(@RequestParam("idUsuario") int idUsuario,
            @RequestPart(name = "imagen", required = false) MultipartFile imagen) {
        try {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            if (imagen != null && !imagen.isEmpty()) {
                byte[] bytes = imagen.getBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                usuario.setImagenFile(base64);
            }
            Result Result = usuarioJPADAOImplementation.UpdateImagen(usuario);
            if (Result.Correct) {
                return ResponseEntity.ok(Result);

            } else {
                return ResponseEntity.badRequest().body(Result);
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
                return ResponseEntity.badRequest().body(Result);
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }

    }

    @PostMapping(value = "validarcarga", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity ValidarArchivo(@RequestPart(name = "archivo") MultipartFile archivo) {
        Result Result = null;
        try {
            if (archivo != null) {
                String rutaBase = System.getProperty("user.dir");
                String rutaCarpeta = "src/main/resources/archivosCM";
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
                String NombreArchivo = fecha + archivo.getOriginalFilename();
                String rutaArchivo = rutaBase + "/" + rutaCarpeta + "/" + NombreArchivo;
                String extension = archivo.getOriginalFilename().split("\\.")[1];
                List<Usuario> Usuarios = null;

                if (extension.contains("txt")) {
                    archivo.transferTo(new File(rutaArchivo));
                    Usuarios = LecturaArchivoTxt(new File(rutaArchivo));
                } else if (extension.contains("xlsx")) {
                    archivo.transferTo(new File(rutaArchivo));
                    Usuarios = LecturaArchivoXLSX(new File(rutaArchivo));
                } else {
                    System.out.println("Extensión Erronea");
                }

                List<ErroresArchivo> errores = ValidarDatos(Usuarios);
                if (errores.isEmpty()) {
                    System.out.println("Sin errores ");
                    Result = new Result();
                    Result.Correct = true;
                    Result.MessageException = "Archivo validado correctamente";
                    String encriptado = encriptarSHA256(rutaArchivo);
                    Result.Object = encriptado;
                    RegisterLog(encriptado, rutaArchivo, "VALIDADO", "ARCHIVO SIN ERRORES");
                    return ResponseEntity.ok().body(Result);

                } else {
                    System.out.println(errores);
                    List<String> listaStrings = new ArrayList<>();
                    for (ErroresArchivo error : errores) {
                        listaStrings.add(error.toString());
                    }
                    Result = new Result();
                    Result.Correct = false;
                    Result.MessageException = "Errores encontrados en el archivo";
                    Result.Objects = new ArrayList<>(listaStrings);
                    String encriptado = encriptarSHA256(rutaArchivo);
                    Result.Object = encriptado;
                    RegisterLog("-", rutaArchivo, "NO VALIDADO", "ARCHIVO CON ERRORES DE VALIDACION");
                    return ResponseEntity.badRequest().body(Result);
                }
            }
            return ResponseEntity.ok().body(null);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(e);
        }

    }

    @PostMapping("procesarcarga")
    public ResponseEntity ProcesarArchivo(@RequestParam("key") String key) {
        Result Result = null;
        try {

            String[] campos = buscarPorKey(key);
            String rutaArchivo = "";
            if (campos != null && campos.length > 1) {
                Result = new Result();
                rutaArchivo = campos[1].trim();

                if (campos[2].trim().equals("PROCESADO")) {
                    RegisterLog(key, rutaArchivo, "ERROR", "ARCHIVO YA PROCESADO");
                    Result.Correct = false;
                    Result.MessageException = "Este archivo ya ha sido procesado";
                    return ResponseEntity.badRequest().body(Result);
                }
                LocalDateTime fechaHora = LocalDateTime.now();
                LocalDateTime fechaHoraLog = LocalDateTime.parse(campos[3].trim(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

                if (fechaHora.isAfter(fechaHoraLog.plusMinutes(2))) {
                    RegisterLog(key, rutaArchivo, "ERROR", "TIEMPO EXPIRADO PARA PROCESAR EL ARCHIVO");
                    Result.Correct = false;
                    Result.MessageException = "El tiempo para procesar este archivo ha expirado";
                    return ResponseEntity.badRequest().body(Result);
                }
                String extension = rutaArchivo.split("\\.")[1];
                List<Usuario> Usuarios = null;
                if (extension.contains("txt")) {

                    Usuarios = LecturaArchivoTxt(new File(rutaArchivo));

                } else if (extension.contains("xlsx")) {

                    Usuarios = LecturaArchivoXLSX(new File(rutaArchivo));

                }

                Result = usuarioJPADAOImplementation.AddAll(Usuarios);
                if (Result.Correct) {
                    RegisterLog(key, rutaArchivo, "PROCESADO", "ARCHIVO PROCESADO CORRECTAMENTE");
                    return ResponseEntity.ok(Result);
                } else {

                    RegisterLog(key, rutaArchivo, "ERROR AL PROCESAR", "ARCHIVO CON ERRORES AL PROCESAR");
                    return ResponseEntity.badRequest().body(Result);
                }

            } else {
                Result = new Result();
                Result.Correct = false;
                Result.MessageException = "Clave no encontrada";
                RegisterLog(key, rutaArchivo, "ERROR AL PROCESAR", "CLAVE NO ENCONTRADA");

                return ResponseEntity.badRequest().body(Result);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return ResponseEntity.internalServerError().body(e);

        }

    }

    // METODOS AUXILIARES
    public List<Usuario> LecturaArchivoTxt(File archivo) {
        List<Usuario> Usuarios = new ArrayList<>();
        try (BufferedReader bufferedreader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int NumeroLinea = 0;

            while ((linea = bufferedreader.readLine()) != null) {
                NumeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] Datos = linea.split("\\|");
                if (Datos.length < 16) {
                    System.out.println("La Linea " + NumeroLinea + " NO TIENE EL FORMATO CORRECTO");
                    System.out.println("Campos encontrados: " + Datos.length);
                    continue;
                }
                try {
                    Usuario Usuario = new Usuario();
                    Usuario.setUserName(Datos[0]);
                    Usuario.setNombre(Datos[1]);
                    Usuario.setApellidoPaterno(Datos[2]);
                    Usuario.setApellidoMaterno(Datos[3]);
                    Usuario.setEmail(Datos[4]);
                    Usuario.setPassword(Datos[5]);
                    if (Datos.length > 6 && !Datos[6].trim().isEmpty()) {
                        Usuario.setFechaNacimiento(LocalDate.parse(Datos[6].trim()));
                    }
                    Usuario.setSexo(limpiarCampo(Datos[7]));
                    Usuario.setTelefono(limpiarCampo(Datos[8]));
                    Usuario.setCelular(limpiarCampo(Datos[9]));
                    Usuario.setCURP(limpiarCampo(Datos[10]));
                    Usuario.Rol.setIdRol(Integer.parseInt(Datos[11].trim()));

                    Direccion direccion = new Direccion();
                    direccion.setCalle(limpiarCampo(Datos[12]));
                    direccion.setNumeroInterior(limpiarCampo(Datos[13]));
                    direccion.setNumeroExterior(limpiarCampo(Datos[14]));

                    if (Datos.length > 15 && !Datos[15].trim().isEmpty()) {
                        direccion.Colonia.setIdColonia(Integer.parseInt(Datos[15].trim()));
                        direccion.Colonia.Municipio.setIdMunicipio(1);
                        direccion.Colonia.Municipio.Estado.setIdEstado(1);
                        direccion.Colonia.Municipio.Estado.Pais.setIdPais(1);
                    }

                    Usuario.getDirecciones().add(direccion);
                    Usuarios.add(Usuario);

                } catch (Exception e) {

                    System.out.println("Error procesando línea " + NumeroLinea + ": " +
                            e.getMessage());
                    e.printStackTrace();
                }

            }
            System.out.println("Total de usuarios leídos: " + Usuarios.size());

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return Usuarios;
    }

    public List<Usuario> LecturaArchivoXLSX(File archivo) {
        List<Usuario> Usuarios = null;
        try (InputStream inputstream = new FileInputStream(archivo);
                XSSFWorkbook workbook = new XSSFWorkbook(inputstream)) {

            Usuarios = new ArrayList<>();
            XSSFSheet sheet = workbook.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();
            for (Row row : sheet) {
                Usuario Usuario = new Usuario();
                Usuario.setUserName(row.getCell(0).toString());
                Usuario.setNombre(row.getCell(1).toString());
                Usuario.setApellidoPaterno(row.getCell(2).toString());
                Usuario.setApellidoMaterno(row.getCell(3).toString());
                Usuario.setEmail(row.getCell(4).toString());
                Usuario.setPassword(row.getCell(5).toString());

                if (row.getCell(6) != null && row.getCell(6).getCellType() != CellType.BLANK) {

                    Usuario.setFechaNacimiento(LocalDate.parse(row.getCell(6).getLocalDateTimeCellValue()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                }
                Usuario.setSexo(limpiarCampo(row.getCell(7).toString()));
                Usuario.setTelefono(limpiarCampo(fmt.formatCellValue(row.getCell(8))));
                Usuario.setCelular(limpiarCampo(fmt.formatCellValue(row.getCell(9))));
                Usuario.setCURP(limpiarCampo(row.getCell(10).toString()));
                Usuario.Rol.setIdRol((int) row.getCell(11).getNumericCellValue());

                Direccion direccion = new Direccion();
                direccion.setCalle(limpiarCampo(row.getCell(12).toString()));
                direccion.setNumeroExterior(fmt.formatCellValue(row.getCell(14)));
                direccion.setNumeroInterior(fmt.formatCellValue(row.getCell(13)));

                direccion.Colonia.setIdColonia((int) (row.getCell(15).getNumericCellValue()));
                direccion.Colonia.Municipio.setIdMunicipio(1);
                direccion.Colonia.Municipio.Estado.setIdEstado(1);
                direccion.Colonia.Municipio.Estado.Pais.setIdPais(1);
                Usuario.Direcciones.add(direccion);
                Usuarios.add(Usuario);

            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());

        }

        return Usuarios;
    }

    public List<ErroresArchivo> ValidarDatos(List<Usuario> Usuarios) {
        List<ErroresArchivo> errores = new ArrayList<>();
        int fila = 0;

        for (Usuario usuario : Usuarios) {
            fila++;

            BindingResult bindingResult = validationservice.ValidateObject(usuario);

            if (bindingResult.hasErrors()) {
                ErroresArchivo errorArchivo = new ErroresArchivo();
                errorArchivo.fila = fila;
                errorArchivo.dato = "";
                errorArchivo.descripcion = "";

                for (ObjectError objectError : bindingResult.getAllErrors()) {
                    if (objectError instanceof FieldError) {
                        FieldError fieldError = (FieldError) objectError;
                        errorArchivo.dato += fieldError.getField() + " ";
                        errorArchivo.descripcion += fieldError.getDefaultMessage() + " ";
                    }
                }

                errores.add(errorArchivo);
            }
        }

        return errores;
    }

    private String limpiarCampo(String campo) {
        if (campo == null || campo.trim().isEmpty() || campo.trim().equals("null")) {
            return "";
        }
        return campo.trim();
    }

    public static void RegisterLog(String key, String ruta, String status, String detalle) {
        String rutaBase = System.getProperty("user.dir");
        String rutaCarpeta = "/src/main/resources/Logs";
        String archivo = rutaBase + rutaCarpeta + "/LOG_CargaMasiva.txt";

        try {
            FileWriter fileWritter = new FileWriter(archivo, true);
            PrintWriter printWritter = new PrintWriter(fileWritter);

            if (new java.io.File(archivo).length() == 0) {
                printWritter.println("KEY|RUTA|STATUS|FECHAHORA|DETALLE");
            }
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            printWritter.println(key + "|" + ruta + "|" + status + "|" + fechaHora + "|" + detalle);
            printWritter.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static String[] buscarPorKey(String keyBuscada) {
        String rutaBase = System.getProperty("user.dir");
        String rutaCarpeta = "/src/main/resources/Logs";
        String archivo = rutaBase + rutaCarpeta + "/LOG_CargaMasiva.txt";
        File file = new File(archivo);
        if (!file.exists()) {
            System.out.println("El archivo no existe: " + archivo);
            return null;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                if (numeroLinea == 1 && linea.startsWith("KEY|")) {
                    continue;
                }
                String[] campos = linea.split("\\|");
                if (campos.length > 0) {
                    String key = campos[0].trim();
                    if (key.equals(keyBuscada)) {
                        System.out.println(" KEY encontrada: " + keyBuscada);
                        return campos;
                    }
                }
            }

            System.out.println("KEY no encontrada: " + keyBuscada);
            return null;

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    public static String encriptarSHA256(String texto) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(texto.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte byteValue : hash) {
                String hex = Integer.toHexString(0xff & byteValue);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algoritmo SHA-256 no encontrado", e);
        }
    }
}
