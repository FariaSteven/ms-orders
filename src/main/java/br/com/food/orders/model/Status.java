package br.com.food.orders.model;

public enum Status {
    REALIZADO,
    CANCELADO,
    PAGO,
    NAO_AUTORIZADO,
    CONFIRMADO,
    CONFIRMADO_SEM_INTEGRACAO,
    PRONTO,
    SAIU_PARA_ENTREGA,
    ENTREGUE;
}
