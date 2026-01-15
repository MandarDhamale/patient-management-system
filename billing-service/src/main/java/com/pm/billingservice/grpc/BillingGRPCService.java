package com.pm.billingservice.grpc;

import billing.BillingServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGRPCService extends BillingServiceGrpc.BillingServiceImplBase {

}
