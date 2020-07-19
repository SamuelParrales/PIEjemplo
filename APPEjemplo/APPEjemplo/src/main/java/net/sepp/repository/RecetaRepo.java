package net.sepp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.sepp.APPEjemplo.entities.Receta;

/*Sirve como conexion a la base de datos*/
public interface RecetaRepo extends CrudRepository <Receta,Long>{
	List<Receta> findByNombre(String nombre);
	List<Receta> findByPreparacion(String nombre);
}
