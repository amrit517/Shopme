package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin =  entityManager.find(Role.class, 1);
		User user = new User("amritsingh@gmail.com","12345","Amrit","Singh");
		user.addRole(roleAdmin);
		
		User saveUser =  repo.save(user);
		assertThat(saveUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		User user = new User("jaskirat@gmail.com","12345","jaskirat","Singh");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		user.addRole(roleEditor);
		user.addRole(roleAssistant);
		
		User saveUser = repo.save(user);
		assertThat(saveUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers =  repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test 
	public void testUserById() {
		User user =  repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user =  repo.findById(2).get();
		user.setEnabled(true);
		user.setEmail("jassi@gmail.com");
		repo.save(user);
		
	}
	
	@Test
	public void testUpdateUserRole() {
		User user =  repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);
		user.getRoles().remove(roleEditor);
		user.addRole(roleSalesPerson);
		
		repo.save(user);		
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);	
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "amritsingh@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id =3;
		Long count  = repo.countById(id);
		assertThat(count).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 2;
		int pageSize = 4;
		
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
		
		
	}
	
	@Test
	public void testSearchUsers() {
		String Keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 4;
		
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(Keyword,pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
		
	}
}
