package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Usuario;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Direccion;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
public class UsuarioJPADAOImplementation implements IUsuarioJPA {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            TypedQuery<Usuario> query = entityManager.createQuery("FROM Usuario u ORDER BY u.Nombre ASC",
                    Usuario.class);
            List<Usuario> UsuariosJPA = query.getResultList();

            result.Objects = new ArrayList<>(UsuariosJPA);
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.MessageException = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result GetById(int IdUsuario) {
        Result Result = new Result();
        try {
            Usuario Usuario = entityManager.find(Usuario.class, IdUsuario);
            if (Usuario != null) {

                Result.Object = Usuario;
                Result.Correct = true;
            } else {
                Result.Correct = false;
                Result.MessageException = "Recurso no encontrado";
            }

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }
        return Result;
    }

    @Transactional
    @Override
    public Result Add(Usuario UsuarioJPA) {
        Result Result = new Result();
        try {
            UsuarioJPA.setUltimoAcceso(LocalDateTime.now());

            for (Direccion direccion : UsuarioJPA.Direcciones) {
                direccion.Usuario = (UsuarioJPA);
            }
            UsuarioJPA.setUltimoAcceso(LocalDateTime.now());
            UsuarioJPA.setActivo(1);
            entityManager.persist(UsuarioJPA);
            Result.Correct = true;

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

    @Transactional
    @Override
    public Result Update(Usuario UsuarioJPA) {
        Result Result = new Result();
        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, UsuarioJPA.getIdUsuario());
            if (usuarioJPA != null) {

                UsuarioJPA.setUltimoAcceso(LocalDateTime.now());
                UsuarioJPA.setActivo(usuarioJPA.getActivo());
                UsuarioJPA.setImagenFile(usuarioJPA.getImagenFile());

                UsuarioJPA.Direcciones = usuarioJPA.Direcciones;

                entityManager.merge(UsuarioJPA);
                Result.Correct = true;
                Result.Object = UsuarioJPA;

            } else {
                Result.Correct = false;
                Result.MessageException = "Recurso no encontrado";
            }

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

    @Transactional
    @Override
    public Result Delete(int IdUsuario) {
        Result Result = new Result();
        try {
            Usuario UsuarioJPA = entityManager.find(Usuario.class, IdUsuario);
            if (UsuarioJPA != null) {
                entityManager.remove(UsuarioJPA);
                Result.Correct = true;
            } else {
                Result.Correct = false;
            }
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

    @Transactional
    @Override
    public Result UpdateActivo(int IdUsuario) {
        Result Result = new Result();
        try {
            Usuario UsuarioJPA = entityManager.find(Usuario.class, IdUsuario);
            if (UsuarioJPA != null) {
                UsuarioJPA.setUltimoAcceso(LocalDateTime.now());
                if (UsuarioJPA.getActivo() == 0) {
                    UsuarioJPA.setActivo(1);
                } else {
                    UsuarioJPA.setActivo(0);
                }

                entityManager.merge(UsuarioJPA);
                Result.Correct = true;
            } else {
                Result.Correct = false;
                Result.MessageException = "Recurso no encontrado";
            }

            Result.Correct = true;
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

    @Transactional
    @Override
    public Result UpdateImagen(Usuario UsuarioJPA) {
        Result Result = new Result();
        try {
            Usuario usuarioJPA = entityManager.find(Usuario.class, UsuarioJPA.getIdUsuario());
            if (usuarioJPA != null) {
                usuarioJPA.setImagenFile(UsuarioJPA.getImagenFile());
                entityManager.merge(usuarioJPA);
                Result.Correct = true;
            } else {
                Result.Correct = false;
                Result.MessageException = "Recurso no encontrado";
            }
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

    @Transactional
    @Override
    public Result AddAll(List<Usuario> UsuariosJPA) {
        Result Result = new Result();
        try {
            int i = 0;
            int batchSize = 50;
            for (Usuario UsuarioJPA : UsuariosJPA) {

                UsuarioJPA.setUltimoAcceso(LocalDateTime.now());
                UsuarioJPA.setActivo(1);
                i++;
                entityManager.persist(UsuarioJPA);
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
            Result.Correct = true;

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }
        return Result;
    }

    @Override
    public Result Busqueda(Usuario usuarioBusqueda) {
        Result Result = new Result();
        try {
            if (usuarioBusqueda.getNombre() == null) {
                usuarioBusqueda.setNombre("");
            }
            if (usuarioBusqueda.getApellidoPaterno() == null) {
                usuarioBusqueda.setApellidoPaterno("");
            }
            if (usuarioBusqueda.getApellidoMaterno() == null) {
                usuarioBusqueda.setApellidoMaterno("");
            }
            if (usuarioBusqueda.getSexo() == null) {
                usuarioBusqueda.setSexo("");
            }
            String consultaJPQL = "SELECT u FROM Usuario u WHERE LOWER(u.Nombre) LIKE LOWER(CONCAT('%', NVL(:pNombre,''), '%'))\n"
                    +
                    "AND LOWER(u.ApellidoPaterno) LIKE LOWER(CONCAT('%', NVL(:pApellidoPaterno,''), '%'))\n" +
                    "AND LOWER(u.ApellidoMaterno) LIKE LOWER(CONCAT('%', NVL(:pApellidoMaterno,''), '%'))\n" +
                    "AND LOWER(u.Sexo) LIKE LOWER(CONCAT('%', :pSexo, '%'))";

            if (usuarioBusqueda.Rol.getIdRol() != 0) {
                consultaJPQL += "\nAND u.Rol.IdRol = :pIdRol\n";
            }
            TypedQuery<Usuario> query = entityManager.createQuery(consultaJPQL + "ORDER BY u.Nombre ASC",
                    Usuario.class);
            query.setParameter("pNombre", "%" + usuarioBusqueda.getNombre().toLowerCase() + "%");
            query.setParameter("pApellidoPaterno", "%" + usuarioBusqueda.getApellidoPaterno().toLowerCase() + "%");
            query.setParameter("pApellidoMaterno", "%" + usuarioBusqueda.getApellidoMaterno().toLowerCase() + "%");
            if (usuarioBusqueda.Rol.getIdRol() != 0) {

                query.setParameter("pIdRol", usuarioBusqueda.Rol.getIdRol());
            }
            query.setParameter("pSexo", "%" + usuarioBusqueda.getSexo().toLowerCase() + "%");

            List<Usuario> usuariosJPA = query.getResultList();

            Result.Objects = new ArrayList<>(usuariosJPA);
            Result.Correct =true;

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

}
