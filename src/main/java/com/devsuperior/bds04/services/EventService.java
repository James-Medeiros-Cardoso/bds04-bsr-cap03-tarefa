package com.devsuperior.bds04.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Transactional(readOnly=true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> list=eventRepository.findAll(pageable); //findAllPaged = retorna uma página (Page)
		
		return list.map(x -> new EventDTO(x));
		
		//List<ProductDTO> listDto=list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
		/*List<ProductDTO> listDto=new ArrayList<>();
		for(Product cat : list)
		{
			listDto.add(new ProductDTO(cat));
		}
		return listDto;*/
	}
	
	@Transactional(readOnly=true)
	public EventDTO findById(Long id) {
		Optional<Event> obj=eventRepository.findById(id);
		Event entity=obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found."));
		return new EventDTO(entity);
	}

	/*@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity=new Product();
		copyDtoToEntity(dto, entity); //entity.setName(dto.getName());
		entity=repository.save(entity);
		return new ProductDTO(entity);
	}*/

	/*@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity=eventRepository.getOne(id); //getOne para atualizar dados
			
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			entity.setCity(new City(dto.getCityId(), null));
		
			entity=eventRepository.save(entity);
			return new EventDTO(entity);
		}
		catch(EntityNotFoundException e) { //EntityNotFoundException = exceção da JPA
			throw new ResourceNotFoundException("Id not found "+id);
		}
	}*/

	/*public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}*/
}
