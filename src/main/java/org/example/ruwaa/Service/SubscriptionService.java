package org.example.ruwaa.Service;

import lombok.RequiredArgsConstructor;
import org.example.ruwaa.Api.ApiException;
import org.example.ruwaa.Model.Customer;
import org.example.ruwaa.Model.Subscription;
import org.example.ruwaa.Model.Users;
import org.example.ruwaa.Repository.CustomerRepository;
import org.example.ruwaa.Repository.SubscriptionRepository;
import org.example.ruwaa.Repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService
{
    private final SubscriptionRepository subscriptionRepository;
    private final CustomerRepository customerRepository;
    private final PaymentService paymentService;

    public void subscribe(String username){
        Customer c = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        paymentService.processPayment(15.0,c.getUsers().getCards().get(0));
        if(c.getSubscription() != null){
            c.getSubscription().setEnd_date(c.getSubscription().getEnd_date().plusMonths(1));
            subscriptionRepository.save(c.getSubscription());
            return;
        }
        Subscription sub = new Subscription();
        sub.setCustomer(c);
        sub.setSubscription_date(LocalDateTime.now());
        sub.setSubscription_date(LocalDateTime.now().plusMonths(1));
        subscriptionRepository.save(sub);
    }

    public void giftSubscribe(String username,String sender){
        Customer gifer = customerRepository.findCustomerByUsername(sender).orElseThrow(() -> new ApiException("user not found"));
        if(gifer.getUsers().getCards() == null){
            throw new ApiException("you dont have cards");
        }
        Customer c = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        paymentService.processPayment(15.0,gifer.getUsers().getCards().get(0));
        if(c.getSubscription() != null){
            c.getSubscription().setEnd_date(c.getSubscription().getEnd_date().plusMonths(1));
            subscriptionRepository.save(c.getSubscription());
            return;
        }
        Subscription sub = new Subscription();
        sub.setCustomer(c);
        sub.setSubscription_date(LocalDateTime.now());
        sub.setSubscription_date(LocalDateTime.now().plusMonths(1));
        subscriptionRepository.save(sub);
    }

    public Subscription getSubscription(String username){
        Customer c = customerRepository.findCustomerByUsername(username).orElseThrow(() -> new ApiException("user not found"));
        if(c.getSubscription() == null){
            throw new ApiException("you have never subscribed yet");
        }
        return c.getSubscription();
    }
}
