package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Estado;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class EstadoJPADAOImplementation implements IEstadoJPA {

    @Autowired
    private EntityManager entityManager;


    @Override
    public Result GetgetEstadoByPais(int IdPais) {
        Result Result = new Result();

        try {
            TypedQuery<Estado> query = entityManager.createQuery("FROM Estado e WHERE e.Pais.IdPais = :idPais ORDER BY e.Nombre ASC", Estado.class)
                    .setParameter("idPais", IdPais);
                    
            List<Estado> estadosJPA = query.getResultList();

            Result.Objects = new ArrayList<>(estadosJPA);
            Result.Correct = true;
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

}
