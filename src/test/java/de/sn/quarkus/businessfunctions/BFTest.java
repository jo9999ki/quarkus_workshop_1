package de.sn.quarkus.businessfunctions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.sn.quarkus.businessfunctions.model.Project;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class BFTest {
	
	private static long identifier;
	
	@Test
	@Transactional
	@Order(1)
	public void testPanacheEntityList() {
		//Check preloaded data
		List<Project> listProjects = Project.listAll();
		assertEquals("Test Projekt",listProjects.get(0).name);	
		
		//List test data hierarchy
		/*
		for (int i = 0; i < listProjects.size(); i++) {
			Project project = listProjects.get(i);
			System.out.println("Project: id= " + project.id +  " name= " + project.name);
			for (int j = 0; j < project.Items.size(); j++) {
				Item item = project.Items.get(j);
				Long parentItemId = -1L;
				if (item.item != null) {
					parentItemId = item.item.id; 
				}
				System.out.println("Item: id=" + item.id + " parent item id = " + parentItemId + " name=" + item.name + " level=" + item.level + " imageURL=" + item.imageURL);
			}
		}*/
	}
	@Test
	@Transactional
	@Order(2)
	public void testPanacheAddNewRecordAndFindByNameLike() {
		//Add new record
		Project newProject = new Project();
		newProject.id= null;
		newProject.name = "MyTestProject";
		newProject.persist();	
		
		//Find first locomotive with certain address
		List<Project> myProject = Project.findAllByNameLike("My");
		assertEquals(1, myProject.size());
		BFTest.identifier = myProject.get(0).id;
	}
	
	@Test
	@Transactional
	@Order(3)
	public void testPanacheFindAllRecordsWithPaging() {
		//Find all records with paging
		PanacheQuery<Project> pagedProjectList = Project.findAll();
		// make it use pages of 25 entries at a time
		pagedProjectList.page(Page.ofSize(25));
		assertEquals(1, pagedProjectList.pageCount());
		// get the first page
		List<Project> firstPage = pagedProjectList.list();
		assertThat("listsize", firstPage.size() > 0);		
		// get the xxx page
		List<Project> page2 = pagedProjectList.page(Page.of(2, 25)).list();
		assertEquals(0, page2.size());
	}
	
	@Test
	@Transactional
	@Order(4)
	public void testPanacheUpdateRecord() {
		//Find first locomotive with certain address
		Project myProject =  Project.findById(BFTest.identifier);
	    myProject.name="UpdatedName";
		myProject.persist();
	    Project updatedProject = Project.findById(BFTest.identifier);
		assertEquals("UpdatedName", updatedProject.name);
	}
	
	@Test
	@Transactional
	@Order(5)
	public void testPanacheDeleteRecord() {
		Project.deleteById(BFTest.identifier);
		Project deletedProject = Project.findById(BFTest.identifier);
		assertEquals(null, deletedProject);
	}
	@Test
    public void testHelloEndpoint() {
        given()
          .when().get("/businessfunctions")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}