package soa.eip;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * We create a Processor class, as recommended on Apache Camel guide
 * https://camel.apache.org/manual/latest/processor.html
 *
 */

public class SeachProcessor implements Processor {
    private static final String MAX_N_REGEX = "max:[0-9]+";
    private static final String COUNT_HEADER_NAME = "count";

    public void process(Exchange exchange) {
        String[] payloadParts = exchange.getIn().getBody(String.class).split(" ");
        String body = "";
        String max = "";

        for(String s : payloadParts) {
            if(s.matches(MAX_N_REGEX)) {
                max = s.split(":")[1];
            } else {
                body += s + " ";
            }
        }
        exchange.getIn().setHeader(COUNT_HEADER_NAME, max);
        exchange.getIn().setBody(body);
    }

}
