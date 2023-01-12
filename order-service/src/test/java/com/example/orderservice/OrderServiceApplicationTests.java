package com.example.orderservice;

import com.example.orderservice.book.Book;
import com.example.orderservice.book.BookClient;
import com.example.orderservice.order.domain.Order;
import com.example.orderservice.order.domain.OrderStatus;
import com.example.orderservice.order.web.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests extends AbstractTestContainersConfig {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookClient bookClient;

    @Test
    void shouldReturnOrdersWhenCallGet() {
        var bookIsbn = "1234567893";
        var book = new Book(bookIsbn, "Title", "Author", 9.90);
        when(bookClient.getBookByIsbn(bookIsbn)).thenReturn(Mono.just(book));
        var orderRequest = new OrderRequest(bookIsbn, 1);
        var expectedOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();
        assertThat(expectedOrder).isNotNull();

        webTestClient.get().uri("/orders")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Order.class)
                .value(orders -> assertThat(orders.stream()
                        .filter(order -> order.bookIsbn().equals(bookIsbn))
                        .findAny())
                        .isNotEmpty());
    }

    @Test
    void shouldOrderAcceptedWhenPostRequestAndBookExists() {
        var bookIsbn = "1234567899";
        var book = new Book(bookIsbn, "Title", "Author", 9.90);
        when(bookClient.getBookByIsbn(bookIsbn)).thenReturn(Mono.just(book));
        var orderRequest = new OrderRequest(bookIsbn, 3);

        var createdOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.bookName()).isEqualTo(book.title() + " - " + book.author());
        assertThat(createdOrder.bookPrice()).isEqualTo(book.price());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.ACCEPTED);
    }

    @Test
    void shouldOrderRejectedWhenPostRequestAndBookNotExists() {
        var bookIsbn = "1234567894";
        when(bookClient.getBookByIsbn(bookIsbn)).thenReturn(Mono.empty());
        var orderRequest = new OrderRequest(bookIsbn, 3);

        var createdOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.bookIsbn()).isEqualTo(orderRequest.isbn());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }

}
