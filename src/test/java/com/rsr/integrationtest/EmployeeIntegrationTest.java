package com.rsr.integrationtest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.integrationtest.domain.Employee;
import com.rsr.integrationtest.exception.DocumentNotFoundException;
import com.rsr.integrationtest.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API = "/api/employees";

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws JsonProcessingException, Exception{
        // given
        Employee employee = Employee.builder()
            .firstName("Ricardo")
            .lastName("Serrano")
            .email("ricardo@gmail.com")
            .build();

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(API)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.not(Matchers.blankOrNullString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(employee.getEmail())))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().reason(Matchers.nullValue()));            
            


    }
    

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
        // given
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Javier").lastName("Lopez").email("javierlpz@yahoo.com").build());
        listOfEmployees.add(Employee.builder().firstName("Sandra").lastName("Robles").email("roblesandra@outlook.com").build());

        employeeRepository.saveAll(listOfEmployees);

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(API));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(listOfEmployees.size())))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().reason(Matchers.nullValue()));            

    }

    @Test
    public void givenStringId_whenFindEmployeeById_thenReturnEmployee() throws Exception{
        // given 
        Employee employee = Employee.builder()
                .firstName("Ramses")
                .lastName("Molina")
                .email("ramses_molina@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(API + "/{id}", employee.getId()));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.not(Matchers.blankOrNullString())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(employee.getEmail())))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().reason(Matchers.nullValue()));
    }

    @Test
    public void givenInvalidStringId_whenFindEmployeeById_thenThrowDocumentNotFoundException() throws Exception{
        // given 
        Employee employee = Employee.builder()
                .firstName("Ramses")
                .lastName("Molina")
                .email("ramses_molina@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(API + "/{id}", "invalidId123"));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Not Found")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Document with that id do not exist")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.path", Matchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DocumentNotFoundException));

            
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
        // given
        Employee savedEmployee = Employee.builder()
            .firstName("Fede")
            .lastName("Juares")
            .email("fedejuarez31@outlook.com")
            .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
            .firstName("Federica")
            .lastName("Juarez")
            .email("fejuarez31@outlook.com")
            .build();

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(API + "/{id}", savedEmployee.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)));
            
        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(savedEmployee.getId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(updatedEmployee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(updatedEmployee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(updatedEmployee.getEmail())))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().reason(Matchers.nullValue()));
    }

    @Test
    public void givenInvalidStringId_whenUpdateEmployee_thenThrowDocumentNotFoundException() throws Exception{
        // given
        Employee savedEmployee = Employee.builder()
            .firstName("Fede")
            .lastName("Juares")
            .email("fedejuarez31@outlook.com")
            .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
            .firstName("Federica")
            .lastName("Juarez")
            .email("fejuarez31@outlook.com")
            .build();

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(API + "/{id}", "invalidId123")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Not Found")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Document with that id do not exist")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.path", Matchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DocumentNotFoundException));
    }

    @Test
    public void givenStringId_whenDeleteEmployeeById_thenReturnDeleteMessage() throws Exception{

        // given
        Employee savedEmployee = Employee.builder()
            .firstName("Fede")
            .lastName("Juares")
            .email("fedejuarez31@outlook.com")
            .build();
        employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(API + "/{id}", savedEmployee.getId()));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("Employee with id: " + savedEmployee.getId()+ " successfully deleted"))
            .andExpect(MockMvcResultMatchers.content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_16)));

    }

    @Test
    public void givenInvalidStringId_whenDeleteEmployeeById_thenThrowDocumentNotFoundException() throws Exception{
        // given
        Employee savedEmployee = Employee.builder()
            .firstName("Fede")
            .lastName("Juares")
            .email("fedejuarez31@outlook.com")
            .build();
        employeeRepository.save(savedEmployee);


        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(API + "/{id}", "invalidId123"));

        // then
        response.andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(404)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.is("Not Found")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Document with that id do not exist")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.path", Matchers.notNullValue()))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DocumentNotFoundException));
    }
}
