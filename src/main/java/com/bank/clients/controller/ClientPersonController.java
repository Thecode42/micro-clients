package com.bank.clients.controller;

import com.bank.clients.client.service.ClientServices;
import com.bank.clients.dto.ClientPersonRequest;
import com.bank.clients.dto.ClientPersonResponse;
import com.bank.clients.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientPersonController {

    private final ClientServices clientServices;


    @Autowired
    public ClientPersonController(ClientServices clientServices) {
        this.clientServices = clientServices;
    }

    @GetMapping("/obtener")
    public  ResponseEntity<List<ClientPersonResponse>> getAllClientPersonOrOne(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "todo", required = false) Boolean todo
    ){
        return ResponseEntity.ok(clientServices.getAllClientPersonOrOne(id,todo));
    }

    @PostMapping(path = "/crear")
    public ResponseEntity<MessageResponse> createClientPerson(@RequestBody ClientPersonRequest clientPersonRequest){
        return ResponseEntity.ok(clientServices.createClientPerson(clientPersonRequest));
    }

    //Actualizamos parcialmente al cliente, no se actualiza la identificacion
    @PatchMapping("/actualizar")
    public ResponseEntity<MessageResponse> updateClientPerson(@RequestBody ClientPersonRequest clientPersonRequest){
        return ResponseEntity.ok(clientServices.updateClientPerson(clientPersonRequest));
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<MessageResponse> deleteClientPerson(@RequestParam(value = "id", required = true) Long id){
        return ResponseEntity.ok(clientServices.deleteClientPerson(id));
    }
}
