package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderCreateResponse;
import kitchenpos.dto.OrderFindResponse;
import kitchenpos.dto.OrderStatusChangeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderCreateResponse create(final OrderCreateRequest orderCreateRequest) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderCreateRequest.getOrderLineItems());
        validateOrderLineItems(orderLineItems);

        final OrderTable orderTable = orderTableDao.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        validateIfTableEmpty(orderTable);

        final Order savedOrder = orderDao.save(
                new Order(
                        orderTable.getId(),
                        OrderStatus.COOKING.name(),
                        LocalDateTime.now(),
                        orderLineItems
                )
        );
        updateOrderLineItemsByOrderId(savedOrder, orderCreateRequest.getOrderLineItems());

        return OrderCreateResponse.from(savedOrder);
    }

    private void validateOrderLineItems(final OrderLineItems orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final long orderMenuIdCounts = menuDao.countByIdIn(orderLineItems.getOrderLineItemMenuIds());
        if (!orderLineItems.hasValidMenus(orderMenuIdCounts)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIfTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void updateOrderLineItemsByOrderId(final Order order, final List<OrderLineItem> orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemDao.update(orderLineItem);
        }
        order.addOrderIdsToOrderLineItems(order.getId());
    }

    public List<OrderFindResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(OrderFindResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderStatusChangeResponse changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        validateChangableStatus(savedOrder);
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.update(savedOrder);
        return new OrderStatusChangeResponse(savedOrder.getOrderStatus());
    }

    private void validateChangableStatus(final Order savedOrder) {
        if (savedOrder.isCompletionOrder()) {
            throw new IllegalArgumentException();
        }
    }
}
