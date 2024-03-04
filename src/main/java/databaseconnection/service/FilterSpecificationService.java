package databaseconnection.service;

import databaseconnection.dto.HotelRequestDto;
import databaseconnection.dto.SearchRequestDto;
import databaseconnection.model.Hotel;
import databaseconnection.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilterSpecificationService<T> {
    @Autowired
    private HotelRepository hotelRepository;
    public Specification<T> getSearchSpecification(SearchRequestDto searchRequestDto){
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(searchRequestDto.getColumn()),searchRequestDto.getValue());
            }
        };
    }
    public Specification<T> getSearchSpecification(List<SearchRequestDto> searchRequestDtos, HotelRequestDto.GlobalOperator globalOperator){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchRequestDto requestDto : searchRequestDtos) {
                switch (requestDto.getOperation()){
                    case LIKE:
                        Predicate like = criteriaBuilder.equal(root.get(requestDto.getColumn()),requestDto.getValue());
                        predicates.add(like);
                        break;
                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(requestDto.getColumn()),"%"+requestDto.getValue()+"%");
                        predicates.add(equal);
                        break;
                    default:
                        throw  new IllegalStateException("Unexpected Value : "+"");
                }
            }
            if(globalOperator.equals(HotelRequestDto.GlobalOperator.AND)) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
    }
    public List<Hotel> findSpecification(Specification<Hotel> specification){
        return hotelRepository.findAll(specification);
    }
 }
