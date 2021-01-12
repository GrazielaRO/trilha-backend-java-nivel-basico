package br.com.zup.estrelas.customerbase.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zup.estrelas.customerbase.dto.CustomerDTO;
import br.com.zup.estrelas.customerbase.entities.Customer;
import br.com.zup.estrelas.customerbase.exceptions.BusinessRuleException;
import br.com.zup.estrelas.customerbase.repositories.CustomerRepository;
import br.com.zup.estrelas.customerbase.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	private static final String CUSTOMER_NOT_FOUND = "CUSTOMER NOT FOUND.";
	private static final String CUSTUMER_ALREADY_EXISTS = "CUSTUMER ALREADY EXISTS IN THE DATABASE.";
	
	@Autowired
	CustomerRepository repository;
	
	@Override
	public Customer insert(CustomerDTO customerDTO) throws BusinessRuleException {
		if (repository.existsByCpf(customerDTO.getCpf())) {
			throw new BusinessRuleException (CUSTUMER_ALREADY_EXISTS);
		}
		Customer customer = insertCustomer(customerDTO);
		return repository.save(customer);
	}
	
	@Override
	public List<Customer> findAll() {
		return (List<Customer>) repository.findAll();
	}
	
	@Override
	public Customer find(String cpf) throws BusinessRuleException {
		return repository.findByCpf(cpf).orElseThrow(() -> new BusinessRuleException(CUSTOMER_NOT_FOUND));
	}
	
	@Override
	public Customer updateData(String cpf, CustomerDTO customerDTO) throws BusinessRuleException {
		Customer customer = repository.findByCpf(cpf).orElseThrow(() -> new BusinessRuleException(CUSTOMER_NOT_FOUND));
		
		BeanUtils.copyProperties(customerDTO, customer);
		
		return repository.save(customer);
	}
	
	@Override
	public void delete(String cpf) throws BusinessRuleException {
		Customer customer = repository.findByCpf(cpf).orElseThrow(() -> new BusinessRuleException(CUSTOMER_NOT_FOUND));
		
		repository.delete(customer);
	}
	
	private Customer insertCustomer(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDTO, customer);
		
		return customer;
	}

}
