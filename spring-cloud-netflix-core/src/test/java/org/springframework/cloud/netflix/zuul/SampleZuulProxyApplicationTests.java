package org.springframework.cloud.netflix.zuul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.netflix.zuul.ZuulFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleZuulProxyApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class SampleZuulProxyApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private RouteLocator routes;

	@Autowired
	private ZuulHandlerMapping mapping;

	@Autowired
	private ZuulConfiguration configuration;

	@Test
	public void deleteOnSelfViaSimpleHostRoutingFilter() {
		routes.getRoutes().put("/self/**", "http://localhost:" + port + "/local");
		mapping.registerHandlers(routes.getRoutes());
		ResponseEntity<String> result = new TestRestTemplate().exchange("http://localhost:" + port + "/self/1",
				HttpMethod.DELETE, new HttpEntity<Void>((Void) null), String.class);
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void registerCustomFilters() {
		Map<String, ZuulFilter> filters = (Map<String, ZuulFilter>) ReflectionTestUtils.getField(configuration, "filters");
		assertTrue("Filters should be > 1", filters.size() > 1);
	}

}
