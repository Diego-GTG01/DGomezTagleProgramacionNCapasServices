package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Pais;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class PaisJPADAOImplementation implements IPaisJPA {

    @Autowired
    private EntityManager entityManager;


    @Override
    public Result GetAll() {
        Result Result = new Result();
        try {
            TypedQuery<Pais> query = entityManager.createQuery("From Pais ORDER BY Nombre ASC", Pais.class);
            List<Pais> paisesJPA = query.getResultList();
            
            Result.Objects = new ArrayList<>(paisesJPA);
            Result.Correct = true;
        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;
        }

        return Result;
    }

}
