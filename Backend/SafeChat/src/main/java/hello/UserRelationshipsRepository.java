package hello;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import hello.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRelationshipsRepository extends CrudRepository<UserRelationships, Integer> {
	public List<UserRelationships> findByFromUsername(String fromusername);
}