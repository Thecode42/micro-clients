package com.bank.clients;

import com.bank.clients.client.enums.ClientStatus;
import com.bank.clients.client.model.Cliente;
import com.bank.clients.client.model.Persona;
import com.bank.clients.client.repository.ClienteRepository;
import com.bank.clients.client.repository.PersonaRepository;
import com.bank.clients.client.service.ClientServices;
import com.bank.clients.dto.ClientPersonRequest;
import com.bank.clients.dto.ClientPersonResponse;
import com.bank.clients.dto.MessageResponse;
import com.bank.clients.exception.ClienteNotFoundException;
import com.bank.clients.mensajes.MessagesClient;
import com.bank.clients.mensajes.MessagesException;
import com.bank.clients.util.GenerateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServicesTest {
    public static final String DATOS_EN_LA_BASE_DE_DATOS = "Error al acceder o registrar los datos en la base de datos";
    public static final String DATA_ACCESS_EXCEPTION = "Error al acceder o registrar los datos en la base de datos";
    @InjectMocks
    private ClientServices clientServices;
    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private GenerateUtil generateUtil;
    @Mock
    private MessagesClient messagesClient;
    @Mock
    private MessagesException messagesException;

    private static final String MESSAGE_SUCCESS_CREATED = "Cliente creado";
    private static final String MESSAGE_SUCCESS_UPDATED = "Cliente actualizado ";
    private static final String MESSAGE_SUCCESS_DELETED = "Cliente eliminado";
    private static final String MESSAGE_CLIENT_NOT_FOUND = "Cliente no existe";



    @Test
    void testGetAllClientPersonOrOne_withId(){
        long id = 1;
        Cliente cliente = getCliente(id);
        Persona persona = getPersona(id, cliente);
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        List<ClientPersonResponse> clientes = clientServices.getAllClientPersonOrOne(id,null);
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
    }
    @Test
    void testGetAllClientPersonOrOne_withTodo(){
        List<Persona> personas = List.of(getPersona(1, new Cliente()), getPersona(1, new Cliente()));
        when(personaRepository.findAll()).thenReturn(personas);
        List<ClientPersonResponse> clientes = clientServices.getAllClientPersonOrOne(null,true);
        assertNotNull(clientes);
        assertEquals(personas.size(), clientes.size());
    }

    @Test
    void testGetAllClientPersonOrOne_withNonExistentId(){
        long id = 12;
        when(messagesException.getNotfound()).thenReturn(MESSAGE_CLIENT_NOT_FOUND);
        when(personaRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClienteNotFoundException.class, () -> clientServices.getAllClientPersonOrOne(id,false));
        verify(personaRepository, times(1)).findById(id);
    }
    @Test
    void testGetAllClientPersonOrOne_withAllDataAccessConnection(){
        when(personaRepository.findAll()).thenThrow(new DataAccessException(DATOS_EN_LA_BASE_DE_DATOS){});
        assertThrows(DataAccessException.class, () -> clientServices.getAllClientPersonOrOne(null,true));
    }
    @Test
    void testGetAllClientPersonOrOne_withIdDataAccessException(){
        List<ClientPersonResponse> clientes = clientServices.getAllClientPersonOrOne(null,false);
        assertTrue(clientes.isEmpty());
    }
    @Test
    void testCreateClientPerson_whenSaveClient(){
        when(messagesClient.getSuccess()).thenReturn(MESSAGE_SUCCESS_CREATED);
        ClientPersonRequest clientPersonRequest = new ClientPersonRequest();
        when(personaRepository.save(any(Persona.class))).thenReturn(new Persona());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(new Cliente());
        MessageResponse response = clientServices.createClientPerson(clientPersonRequest);
        assertEquals(MESSAGE_SUCCESS_CREATED, response.getMessage());
    }
    @Test
    void testCreateClientPerson_whenIsDataAccessException(){
        ClientPersonRequest clientPersonRequest = getClientPersonRequest();
        when(personaRepository.save(any(Persona.class))).thenThrow(new DataAccessException(DATA_ACCESS_EXCEPTION){});
        assertThrows(DataAccessException.class, () -> clientServices.createClientPerson(clientPersonRequest));
    }

    @Test
    void testUpdateClientPerson_whenSaveClient(){
        when(messagesClient.getUpdate()).thenReturn(MESSAGE_SUCCESS_UPDATED);
        ClientPersonRequest clientPersonRequest = getClientPersonRequest();
        when(personaRepository.findById(any(Long.class))).thenReturn(Optional.of(getPersona(1, getCliente(1L))));
        when(personaRepository.save(any(Persona.class))).thenReturn(new Persona());
        MessageResponse response = clientServices.updateClientPerson(clientPersonRequest);
        assertEquals(MESSAGE_SUCCESS_UPDATED, response.getMessage());
    }
    @Test
    void testDeleteClientPerson(){
        when(messagesClient.getDelete()).thenReturn(MESSAGE_SUCCESS_DELETED);
        when(personaRepository.findById(any(Long.class))).thenReturn(Optional.of(getPersona(1, getCliente(1L))));
        when(personaRepository.save(any(Persona.class))).thenReturn(new Persona());
        MessageResponse response = clientServices.deleteClientPerson(1L);
        assertEquals(MESSAGE_SUCCESS_DELETED, response.getMessage());
    }
    @Test
    void testDeleteClientPerson_whenIsDataAccessException(){
        when(personaRepository.findById(any(Long.class))).thenReturn(Optional.of(getPersona(1, getCliente(1L))));
        when(personaRepository.save(any(Persona.class))).thenThrow(new DataAccessException(DATOS_EN_LA_BASE_DE_DATOS){});
        assertThrows(DataAccessException.class, () -> clientServices.deleteClientPerson(1L));
        verify(personaRepository, times(1)).findById(1L);
    }

    private static ClientPersonRequest getClientPersonRequest() {
        ClientPersonRequest clientPersonRequest = new ClientPersonRequest();
        clientPersonRequest.setId(1L);
        clientPersonRequest.setNombre("Jose");
        clientPersonRequest.setGenero("M");
        clientPersonRequest.setEdad(21);
        clientPersonRequest.setDireccion("Av Amazonas E32");
        clientPersonRequest.setTelefono("099999999");
        return clientPersonRequest;
    }

    private static Cliente getCliente(long id) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setContrasena("xxxx");
        cliente.setEstado(ClientStatus.ACTIVE.getDescription());
        return cliente;
    }

    private static Persona getPersona(long id, Cliente cliente) {
        Persona persona = new Persona();
        persona.setId(id);
        persona.setIdentificacion("1234567890");
        persona.setNombre("Jose");
        persona.setGenero("M");
        persona.setEdad(23);
        persona.setDireccion("Av La Rioja 23A");
        persona.setTelefono("0999999999");
        persona.setCliente(cliente);
        return persona;
    }

}
