package com.crud.rental.service;

import com.crud.rental.domain.Damage;
import com.crud.rental.exception.DamageNotFoundException;
import com.crud.rental.repository.DamageRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DamageService {
    private final DamageRespository damageRespository;

    public List<Damage> getAllDamages(){
        return damageRespository.findAll();
    }
    public Damage saveDamage(Damage damage){
     return    damageRespository.save(damage);
    }
    public void delateDamegeById(final  long damageId){
        Damage damage = damageRespository.findById(damageId).orElseThrow(DamageNotFoundException::new);
       damageRespository.delete(damage);
    }
}
