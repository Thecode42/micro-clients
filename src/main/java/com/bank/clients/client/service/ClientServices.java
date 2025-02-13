package com.bank.clients.client.service;

import com.bank.clients.client.enums.ClientStatus;
import com.bank.clients.client.model.Cliente;
import com.bank.clients.client.model.Persona;
import com.bank.clients.client.repository.ClienteRepository;
import com.bank.clients.client.repository.PersonaRepository;
import com.bank.clients.dto.ClientPersonRequest;
import com.bank.clients.dto.ClientPersonResponse;
import com.bank.clients.dto.MessageResponse;
import com.bank.clients.exception.ClientException;
import com.bank.clients.exception.ClienteNotFoundException;
import com.bank.clients.mensajes.MessagesClient;
import com.bank.clients.mensajes.MessagesException;
import com.bank.clients.util.GenerateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServices {
    public static final Logger log = LoggerFactory.getLogger(ClientServices.class);
    public static final String ERROR_AL_ACCEDER_A_LOS_REGISTROS_DEL_CLIENTE = "Error al acceder a los registros del cliente: ";
    public static final String ERROR_AL_REGISTRAR_EL_CLIENTE = "Error al registrar el cliente: ";
    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;
    private final GenerateUtil generateUtil;
    private final MessagesException messagesException;
    private final MessagesClient messagesClient;

    @Autowired
    public ClientServices(ClienteRepository clienteRepository,
                          PersonaRepository personaRepository,
                          GenerateUtil generateUtil,
                          MessagesException messagesException,
                          MessagesClient messagesClient) {
        this.clienteRepository = clienteRepository;
        this.personaRepository = personaRepository;
        this.generateUtil = generateUtil;
        this.messagesException = messagesException;
        this.messagesClient = messagesClient;
    }
    @Transactional(readOnly = true)
    public List<ClientPersonResponse> getAllClientPersonOrOne(Long id, Boolean todo) {
        try {
            return Optional.ofNullable(id)
                    .map(this::getClientById)
                    .orElseGet(() -> todo != null && todo ? getAllClients() : Collections.emptyList());
        } catch (DataAccessException e) {
            log.error(ERROR_AL_ACCEDER_A_LOS_REGISTROS_DEL_CLIENTE, e);
            throw e;
        } catch (ClienteNotFoundException cex){
            log.error("Cliente con el id {} no encontrado", id);
            throw cex;
        }
    }

    private List<ClientPersonResponse> getClientById(Long id) {
        return personaRepository.findById(id)
                .map(this::mapToClientPersonResponse)
                .map(Collections::singletonList)
                .orElseThrow(() -> new ClienteNotFoundException(messagesException.getNotfound()));
    }
    private List<ClientPersonResponse> getAllClients() {
        return personaRepository.findAll().stream()
                .map(this::mapToClientPersonResponse)
                .collect(Collectors.toList());
    }

    private ClientPersonResponse mapToClientPersonResponse(Persona persona) {
        return new ClientPersonResponse(
                persona.getIdentificacion(),
                persona.getNombre(),
                persona.getGenero(),
                persona.getEdad(),
                persona.getDireccion(),
                persona.getTelefono(),
                persona.getCliente().getEstado()
        );
    }

    @Transactional
    public MessageResponse createClientPerson(ClientPersonRequest clientPersonRequest) {
        try {
            Persona persona = getCreateOrUpdatePersona(null, clientPersonRequest);
            Persona currentPersoon = personaRepository.save(persona);
            Cliente cliente = getCliente(currentPersoon);
            clienteRepository.save(cliente);
            return new MessageResponse(messagesClient.getSuccess());
        } catch (DataAccessException ex) {
            log.error(ERROR_AL_ACCEDER_A_LOS_REGISTROS_DEL_CLIENTE, ex);
            throw ex;
        } catch (Exception e) {
            log.error(ERROR_AL_REGISTRAR_EL_CLIENTE, e);
            throw new RuntimeException(ERROR_AL_REGISTRAR_EL_CLIENTE,e);
        }
    }

    private Cliente getCliente(Persona currentPerson) {
        Cliente cliente = new Cliente();
        cliente.setContrasena(generateUtil.generateRandomString(7));
        cliente.setEstado(ClientStatus.ACTIVE.getDescription());
        cliente.setIdPersona(currentPerson);
        return cliente;
    }

    @Transactional
    public MessageResponse updateClientPerson(ClientPersonRequest clientPersonRequest) {
        try {
            Persona personaData = personaRepository.findById(clientPersonRequest.getId())
                    .orElseThrow(() -> new ClienteNotFoundException(messagesException.getNotfound()));
            Persona persona = getCreateOrUpdatePersona(personaData, clientPersonRequest);
            Cliente cliente = persona.getCliente();
            cliente.setContrasena(generateUtil.generateRandomString(7));
            persona.setCliente(cliente);
            personaRepository.save(persona);
            return new MessageResponse(messagesClient.getUpdate());
        } catch (DataAccessException ex) {
            log.error(ERROR_AL_ACCEDER_A_LOS_REGISTROS_DEL_CLIENTE, ex);
            throw ex;
        } catch (Exception e) {
            log.error(ERROR_AL_REGISTRAR_EL_CLIENTE, e);
            throw new RuntimeException(ERROR_AL_REGISTRAR_EL_CLIENTE,e);
        }
    }
    @Transactional
    public MessageResponse deleteClientPerson(Long id) {
        try{
            Persona persona = personaRepository.findById(id)
                    .orElseThrow(() -> new ClientException(messagesException.getNotfound()));
            persona.getCliente().setEstado(ClientStatus.INACTIVE.getDescription());
            personaRepository.save(persona);
            return new MessageResponse(messagesClient.getDelete());
        } catch (DataAccessException ex) {
            log.error(ERROR_AL_ACCEDER_A_LOS_REGISTROS_DEL_CLIENTE, ex);
            throw ex;
        } catch (Exception e) {
            log.error(ERROR_AL_REGISTRAR_EL_CLIENTE, e);
            throw new RuntimeException(ERROR_AL_REGISTRAR_EL_CLIENTE,e);
        }
    }

    private static Persona getCreateOrUpdatePersona(Persona persona, ClientPersonRequest clientPersonRequest) {
        if (persona == null) {
            persona = new Persona();
            persona.setIdentificacion(clientPersonRequest.getIdentificacion());
        }
        persona.setId(clientPersonRequest.getId());
        persona.setNombre(clientPersonRequest.getNombre());
        persona.setGenero(clientPersonRequest.getGenero());
        persona.setEdad(clientPersonRequest.getEdad());
        persona.setDireccion(clientPersonRequest.getDireccion());
        persona.setTelefono(clientPersonRequest.getTelefono());
        return persona;
    }
}

