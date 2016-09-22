package ch.daplab.jubilantoctoumbrella;

import java.util.Random;

/**
 * Created by vgrivel on 9/1/16.
 */
public class RandomGenerator implements RandomGeneratorService {

    private String seq;
    private String baseName;
    private int sequence = 1;
    private Random rnd = new Random();

    private String[] countries={"Switzerland", "France", "Germany", "United States", "Spain", "Portugal", "Ethiopia", "Norway"};
    private String[] currencies={"CHF","EUR", "USD"};
    private Merchant[] merchantArray;
    private CreditCard[] creditCardsArray;

   public RandomGenerator(){
       this(1000,10000);
   }

    public RandomGenerator(int nbrMarchant, int nbrCreditCard) {
        baseName =  "executor1-";
        seq = baseName + sequence++;
        creditCardsArray= new CreditCard[nbrCreditCard];
        merchantArray = new Merchant[nbrMarchant];
        init(nbrMarchant, nbrCreditCard);

    }

    public Transaction getRndTransaction() {
        boolean whiteListed = rnd.nextBoolean();


        //TODO payload with dynamic size.
        String payload = String.valueOf(rnd.nextDouble());
        long timestamp= System.currentTimeMillis();
        String currency =currencies[rnd.nextInt(currencies.length)];
        double amount = rnd.nextInt()/100;
        Merchant merchant = merchantArray[rnd.nextInt(merchantArray.length)];
        CreditCard creditCard = creditCardsArray[rnd.nextInt(creditCardsArray.length)];
        seq = baseName + sequence++;

        Transaction transaction = new Transaction(whiteListed, merchant.getMerchantId(), merchant.getMerchantCountry(), payload, seq, timestamp, currency, amount, creditCard.getCcv(),
                creditCard.getCardNumber(), creditCard.getExpiration(), creditCard.getName(), creditCard.getClientCountry());

        return transaction;
    }

    private void init(int nbrMerchant, int nbrCreditCard){


        //generate nbrMarchant
        for (int i = 0; i < nbrMerchant; i++) {
            String merchantId = ""+rnd.nextLong();
            String country = countries[rnd.nextInt(countries.length)];
            Merchant merchant = new Merchant(merchantId, country);
            merchantArray[i]=merchant;
        }

        // generate creditcards

        for (int i = 0; i < nbrCreditCard; i++) {
            int ccv = rnd.nextInt(1000);
            String cardNumber = String.valueOf(rnd.nextLong());//TODO Find a better way with fix length
            String expiration = (rnd.nextInt(12)+1 +"/20"+(rnd.nextInt()%10+16));
            String name = String.valueOf(rnd.nextLong());//TODO with name generator
            String clientCountry = countries[rnd.nextInt(countries.length)];
            creditCardsArray[i]= new CreditCard(ccv,cardNumber,expiration,clientCountry,name);

        }



    }

    private class Merchant{
        public Merchant(String merchantId, String merchantCountry) {
            this.merchantId = merchantId;
            this.merchantCountry = merchantCountry;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantCountry() {
            return merchantCountry;
        }

        public void setMerchantCountry(String merchantCountry) {
            this.merchantCountry = merchantCountry;
        }

        String merchantId;
        String merchantCountry;
    }

    private class CreditCard{
        int ccv;
        String cardNumber;

        public CreditCard(int ccv, String cardNumber, String expiration, String clientCountry, String name) {
            this.ccv = ccv;
            this.cardNumber = cardNumber;
            this.expiration = expiration;
            this.clientCountry = clientCountry;
            this.name = name;
        }

        String expiration;
        String clientCountry;
        String name;
        public int getCcv() {
            return ccv;
        }

        public void setCcv(int ccv) {
            this.ccv = ccv;
        }



        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClientCountry() {
            return clientCountry;
        }

        public void setClientCountry(String clientCountry) {
            this.clientCountry = clientCountry;
        }


    }
}
