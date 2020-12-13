package soa.eip;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

  public static final String DIRECT_URI = "direct:twitter";
  private static final String TWITTER_SEARCH = "twitter-search:${body}";
  private static final String TWITTER_SEARCH_MAX = "twitter-search:${body}?count=${header.count}";
  private static final String MAX_REGEX = ".*max:[0-9]+.*";
  private static final int MAX_REQUEST_COUNT = 5;
  private static final int TIME_PERIOD = 10000;

  @Override
  public void configure() {
    from(DIRECT_URI)
      .log("Body contains \"${body}\"")
      .log("Searching twitter for \"${body}\"!")
      .throttle(MAX_REQUEST_COUNT).timePeriodMillis(TIME_PERIOD)
      .choice()
      .when(body().regex(MAX_REGEX))
        .process(new SeachProcessor())
        .toD(TWITTER_SEARCH_MAX)
        .endChoice()
      .otherwise()
        .toD(TWITTER_SEARCH)
        .endChoice()
      .end()
      .log("Body now contains the response from twitter:\n${body}");
  }
}
