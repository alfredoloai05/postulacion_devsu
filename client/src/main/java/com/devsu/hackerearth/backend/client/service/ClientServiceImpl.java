package com.devsu.hackerearth.backend.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import java.util.stream.Collectors;
import com.devsu.hackerearth.backend.client.model.Client;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private ClientSyncPublisher clientSyncPublisher;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Autowired
	public void setClientSyncPublisher(ClientSyncPublisher clientSyncPublisher) {
		this.clientSyncPublisher = clientSyncPublisher;
	}

	@Override
	public List<ClientDto> getAll() {
		return clientRepository.findAll()
			.stream()
			.map(client -> new ClientDto(
				client.getId(),
				client.getDni(),
				client.getName(),
				client.getPassword(),
				client.getGender(),
				client.getAge(),
				client.getAddress(),
				client.getPhone(),
				client.isActive()
			))
			.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		return clientRepository.findById(id)
			.map(client -> new ClientDto(
				client.getId(),
				client.getDni(),
				client.getName(),
				client.getPassword(),
				client.getGender(),
				client.getAge(),
				client.getAddress(),
				client.getPhone(),
				client.isActive()
			))
			.orElse(null);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		Client client = new Client();

		client.setDni(clientDto.getDni());
		client.setName(clientDto.getName());
		client.setPassword(clientDto.getPassword());
		client.setGender(clientDto.getGender());
		client.setAge(clientDto.getAge());
		client.setAddress(clientDto.getAddress());
		client.setPhone(clientDto.getPhone());
		client.setActive(clientDto.isActive());

		Client savedClient = clientRepository.save(client);

		if (clientSyncPublisher != null){
			clientSyncPublisher.publish(
				savedClient.getId(),
				savedClient.getName(),
				savedClient.isActive()
			);
		}

		return new ClientDto(
			savedClient.getId(),
			savedClient.getDni(),
			savedClient.getName(),
			savedClient.getPassword(),
			savedClient.getGender(),
			savedClient.getAge(),
			savedClient.getAddress(),
			savedClient.getPhone(),
			savedClient.isActive()
		);
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		Client client = clientRepository.findById(clientDto.getId())
			.orElse(null);

		if (client == null){
			return null;
		}

		client.setDni(clientDto.getDni());
		client.setName(clientDto.getName());
		client.setPassword(clientDto.getPassword());
		client.setGender(clientDto.getGender());
		client.setAge(clientDto.getAge());
		client.setAddress(clientDto.getAddress());
		client.setPhone(clientDto.getPhone());
		client.setActive(clientDto.isActive());

		Client savedClient = clientRepository.save(client);

		if (clientSyncPublisher != null){
			clientSyncPublisher.publish(
				savedClient.getId(),
				savedClient.getName(),
				savedClient.isActive()
			);
		}

		return new ClientDto(
			savedClient.getId(),
			savedClient.getDni(),
			savedClient.getName(),
			savedClient.getPassword(),
			savedClient.getGender(),
			savedClient.getAge(),
			savedClient.getAddress(),
			savedClient.getPhone(),
			savedClient.isActive()
		);
	}

	@Override
    public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		Client client = clientRepository.findById(id)
			.orElse(null);

		if (client == null){
			return null;
		}

		client.setActive(partialClientDto.isActive());

		Client savedClient = clientRepository.save(client);

		if (clientSyncPublisher != null){
			clientSyncPublisher.publish(
				savedClient.getId(),
				savedClient.getName(),
				savedClient.isActive()
			);
		}

		return new ClientDto(
			savedClient.getId(),
			savedClient.getDni(),
			savedClient.getName(),
			savedClient.getPassword(),
			savedClient.getGender(),
			savedClient.getAge(),
			savedClient.getAddress(),
			savedClient.getPhone(),
			savedClient.isActive()
		);
    }

	@Override
	public void deleteById(Long id) {

		Client client = clientRepository.findById(id)
			.orElse(null);

		if (clientSyncPublisher != null){
			clientSyncPublisher.publish(
				client.getId(),
				client.getName(),
				false
				);
		}
	}
}
