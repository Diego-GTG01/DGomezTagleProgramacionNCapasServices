package com.risosuit.DGomezTagleProgramacionNCapasMaven.DAO;

import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Result;
import com.risosuit.DGomezTagleProgramacionNCapasMaven.JPA.Rol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class RolJPADAOImplementation implements IRolJPA {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        Result Result = new Result();
        try {
            TypedQuery<Rol> query = entityManager.createQuery("FROM Rol", Rol.class);
            List<Rol> roles = query.getResultList();
            
            if (!roles.isEmpty()) {
                Result.Correct = true;
                Result.Objects = new ArrayList<>(roles);
            }else{
                Result.Correct = false;
                Result.Objects = new ArrayList<>();
            }

        } catch (Exception ex) {
            Result.Correct = false;
            Result.MessageException = ex.getLocalizedMessage();
            Result.ex = ex;

        }
        return Result;
    }

}
