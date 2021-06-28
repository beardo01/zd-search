package com.oreid;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.oreid.model.*;
import com.oreid.repository.Datastore;
import com.oreid.repository.EntityDatastore;
import com.oreid.services.MenuService;
import com.oreid.services.SearchService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ZendeskSearch {

    private static final String ORGANIZATIONS_FILE_NAME = "organizations.json";
    private static final String USERS_FILE_NAME = "users.json";
    private static final String TICKETS_FILE_NAME = "tickets.json";
    private static final String ORGANIZATION_ID_KEY = "organization_id";
    private static final String ID_KEY = "_id";

    private final EntityDatastore datastore = new Datastore();
    private final SearchService searchService = new SearchService(datastore);
    private final MenuService menuService = new MenuService(searchService);

    public static void main(String[] args) {
        ZendeskSearch zendeskSearch = new ZendeskSearch();

        // Load and retrieve entities
        Pair<List<AbstractEntity>,List<SearchType>> entities = zendeskSearch.loadAndRetrieveEntities();

        // Add entities to datastore
        entities.getLeft().forEach(zendeskSearch.datastore::addEntity);

        // Add search types to datastore
        entities.getRight().forEach(zendeskSearch.searchService::addSearchType);

        // Print menu and accept input
        zendeskSearch.menuService.printMenu();
        zendeskSearch.menuService.processInput();
    }

    private Pair<List<AbstractEntity>, List<SearchType>> loadAndRetrieveEntities() {
        Gson gson = new Gson();

        // Retrieve entities
        List<Organization> organizations = retrieveJsonObjects(gson, ORGANIZATIONS_FILE_NAME, Organization::new);
        Map<String, List<User>> userMap = retrieveIndexedEntities(gson, USERS_FILE_NAME, User::new);
        Map<String, List<Ticket>> ticketMap = retrieveIndexedEntities(gson, TICKETS_FILE_NAME, Ticket::new);

        // Add relatedEntities to organization
        organizations.forEach(org -> {
            String organizationId = org.getData().get(ID_KEY).toString();
            userMap.getOrDefault(organizationId, new ArrayList<>()).forEach(org::addRelatedEntity);
            ticketMap.getOrDefault(organizationId, new ArrayList<>()).forEach(org::addRelatedEntity);
        });

        // Get users and tickets out of maps
        List<User> users = userMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Ticket> tickets = ticketMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // Collect all entities
        List<AbstractEntity> allEntities = Stream.of(organizations, users, tickets)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // Collect unique entity types
        List<SearchType> entityTypes = new ArrayList<>();
        entityTypes.add(new SearchType(organizations.get(0)));
        entityTypes.add(new SearchType(users.get(0)));
        entityTypes.add(new SearchType(tickets.get(0)));

        return new ImmutablePair<>(allEntities, entityTypes);
    }

    private <T extends AbstractEntity> Map<String, List<T>> retrieveIndexedEntities(Gson gson, String fileName, Function<JsonObject, T> entityMapper) {
        return retrieveJsonObjects(gson, fileName, entityMapper).stream()
                .collect(Collectors.toMap(
                        entity -> entity.getData().has(ORGANIZATION_ID_KEY) ? entity.getData().get(ORGANIZATION_ID_KEY).toString() : null,
                        entity -> new ArrayList<>(Collections.singleton(entity)),
                        (existingJsonObjects, singleJsonObjectList) -> {
                            existingJsonObjects.addAll(singleJsonObjectList);
                            return existingJsonObjects;
                        }
                ));
    }

    private <T extends AbstractEntity> List<T> retrieveJsonObjects(Gson gson, String fileName, Function<JsonObject, T> entityMapper) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            String jsonData = IOUtils.toString(Objects.requireNonNull(inputStream), String.valueOf(StandardCharsets.UTF_8));
            List<JsonObject> objects = gson.fromJson(jsonData, new TypeToken<List<JsonObject>>() {}.getType());
            return objects.stream()
                    .map(entityMapper)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
