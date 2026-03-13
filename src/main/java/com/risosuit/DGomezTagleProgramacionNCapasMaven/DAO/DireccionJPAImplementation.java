package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Colonia;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Direccion;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository

public class DireccionJPAImplementation implements IDireccionJPA {
    @Autowired
    private EntityManager entityManager;


    @Override
    public Result GetById(int IdDireccion) {
        Result Result = new Result();
        try {
            Direccion DireccionJPA = entityManager.find(Direccion.class, IdDireccion);
            if (DireccionJPA != null) {
                Result.Object = DireccionJPA;
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
    public Result Add(Direccion Direccion, int idUsuario) {
        Result Result = new Result();
        try {
            Usuario UsuarioJPA = entityManager.find(Usuario.class, idUsuario);
            if (UsuarioJPA != null) {
               
                Direccion.Usuario = UsuarioJPA;
                UsuarioJPA.Direcciones.add(Direccion);
                entityManager.merge(UsuarioJPA);
                entityManager.flush();

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
    public Result Update(Direccion DireccionML, int IdUsuario) {
        Result Result = new Result();
        try {
            Direccion DireccionJPA = entityManager.find(
                    Direccion.class,
                    DireccionML.getIdDireccion());
            if (DireccionJPA == null) {
                Result.Correct = false;
                Result.MessageException = "Dirección no encontrada";
                return Result;
            }
            DireccionJPA.setCalle(DireccionML.getCalle());
            DireccionJPA.setNumeroExterior(DireccionML.getNumeroExterior());
            DireccionJPA.setNumeroInterior(DireccionML.getNumeroInterior());
            if (DireccionML.Colonia != null && DireccionML.Colonia.getIdColonia() > 0) {
                Colonia colonia = entityManager.find(Colonia.class, DireccionML.Colonia.getIdColonia());
                DireccionJPA.Colonia=colonia;
            }
            entityManager.merge(DireccionJPA);
            entityManager.flush();
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
    public Result Delete(int idDireccion) {
        Result Result = new Result();
        try {
            Direccion DireccionJPA = entityManager.find(Direccion.class, idDireccion);
            if (DireccionJPA != null) {
                entityManager.remove(DireccionJPA);
                entityManager.flush();
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

}
