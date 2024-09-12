package ro.championsclub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ro.championsclub.constant.DiscountTypeEnum;
import ro.championsclub.dto.request.DiscountRequest;
import ro.championsclub.dto.request.DiscountUpdateRequest;
import ro.championsclub.dto.response.DiscountAdminView;
import ro.championsclub.entity.Discount;
import ro.championsclub.exception.BusinessException;
import ro.championsclub.exception.ResourceConflictException;
import ro.championsclub.mapper.ModelMapper;
import ro.championsclub.repository.DiscountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveDiscount(DiscountRequest request) {
        String name = request.getName();
        String code = request.getCode().toUpperCase();

        if (request.getValidFrom().isAfter(request.getValidTo())) {
            throw new BusinessException("\"Valid from\" must before \"Valid to\" ");
        }

        if (discountRepository.existsByName(name)) {
            throw new ResourceConflictException("Discount with name: " + name + " already exists");
        } else if (discountRepository.existsByCode(code)) {
            throw new ResourceConflictException("Discount with code: " + code + " already exists");
        }

        var discount = Discount.builder()
                .name(name)
                .code(code)
                .type(DiscountTypeEnum.getType(request.getType()))
                .value(request.getValue())
                .usePerUser(request.getUsePerUser())
                .minimumCartTotal(request.getMinimumCartTotal())
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .compatibleWithOther(request.getCompatibleWithOther())
                .build();

        discountRepository.save(discount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void disableDiscount(String name) {
        discountRepository.disable(name);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateDiscount(String name, DiscountUpdateRequest request) {
        if (request.getValidFrom().isAfter(request.getValidTo())) {
            throw new BusinessException("\"Valid from\" must before \"Valid to\" ");
        }

        var discount = discountRepository.getByName(name);

        request.setCode(request.getCode().toUpperCase());

        ModelMapper.map(request, discount);

        discountRepository.save(discount);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DiscountAdminView> getAllActiveDiscounts() {
        List<Discount> discounts = discountRepository.findAllByIsActiveTrue();

        return ModelMapper.mapAll(discounts, DiscountAdminView.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public List<DiscountAdminView> getAllInactiveDiscounts() {
        List<Discount> discounts = discountRepository.findAllByIsActiveFalse();

        return ModelMapper.mapAll(discounts, DiscountAdminView.class);
    }

    public BigDecimal calculateDiscount(Set<Discount> discounts, BigDecimal total) {
        BigDecimal discount = BigDecimal.ZERO;

        if (discounts.isEmpty() || total.compareTo(BigDecimal.ZERO) == 0) {
            return discount;
        }

        Set<Discount> totalCartValueDiscounts = discountTypeFilter(discounts, DiscountTypeEnum.TOTAL_CART_VALUE);
        Set<Discount> percentageDiscounts = discountTypeFilter(discounts, DiscountTypeEnum.PERCENTAGE);

        for (Discount disc : percentageDiscounts) {
            BigDecimal discountValue = BigDecimal.valueOf(disc.getValue());
            final BigDecimal oneHundred = BigDecimal.valueOf(100);

            discount = discount.add(total.multiply(discountValue).divide(oneHundred, RoundingMode.CEILING));

            total = total.subtract(discount);
        }

        for (Discount disc : totalCartValueDiscounts) {
            BigDecimal discountValue = BigDecimal.valueOf(disc.getValue());

            discount = discount.add(discountValue);
        }

        return discount;
    }

    private Set<Discount> discountTypeFilter(Set<Discount> discounts, DiscountTypeEnum type) {
        return discounts.stream()
                .filter(discount -> discount.getType().equals(type))
                .collect(Collectors.toSet());
    }

}
