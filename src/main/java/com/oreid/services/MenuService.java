package com.oreid.services;

import com.google.common.annotations.VisibleForTesting;
import com.oreid.domain.EntityType;

import java.util.Scanner;

import static com.oreid.domain.MenuConstants.*;

public class MenuService {

    private final SearchService searchService;

    public MenuService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void printMenu() {
        System.out.print(MAIN_MENU);
    }

    public void processMenuInput(String input) {
        switch(input) {
            case SEARCH_OPTION:
                processSearchSelect();
                break;
            case FIELDS_OPTION:
                searchService.printSearchTypes();
                break;
            case QUIT_OPTION:
                System.out.print(QUIT_MESSAGE);
                System.exit(0);
            default:
                System.out.print(INVALID_INPUT_MESSAGE);
        }
    }

    private void processSearchSelect() {
        Scanner input = new Scanner(System.in);
        EntityType entityType = getEntityType(input);
        String key = getSearchTerm(input, entityType);
        String value = getSearchValue(input);

        this.searchService.printSearchResults(entityType, key, value);
    }

    @VisibleForTesting
    EntityType getEntityType(Scanner input) {
        EntityType entityType = null;

        do {
            System.out.print(SELECT_ENTITY_MESSAGE);
            String entityString = input.nextLine();

            switch(entityString) {
                case USER_OPTION:
                    entityType = EntityType.USER;
                    break;
                case TICKET_OPTION:
                    entityType = EntityType.TICKET;
                    break;
                case ORGANIZATION_OPTION:
                    entityType = EntityType.ORGANIZATION;
                    break;
                default:
                    System.out.print(INVALID_INPUT_MESSAGE);
            }
        } while (entityType == null);

        return entityType;
    }

    private String getSearchTerm(Scanner input, EntityType entityType) {
        String field = null;

        do {
            System.out.print(SEARCH_TERM_MESSAGE);
            String fieldInput = input.nextLine();

            if(this.searchService.isValidField(entityType, fieldInput)) {
                field = fieldInput;
            } else {
                System.out.print(INVALID_INPUT_MESSAGE);
            }
        } while (field == null);

        return field;
    }

    private String getSearchValue(Scanner input) {
        System.out.print(SEARCH_VALUE_MESSAGE);
        return input.nextLine();
    }

}
