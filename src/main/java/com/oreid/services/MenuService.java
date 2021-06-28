package com.oreid.services;

import com.google.common.annotations.VisibleForTesting;
import com.oreid.domain.EntityType;

import java.util.Scanner;

import static com.oreid.domain.MenuConstants.*;

public class MenuService {

    private final SearchService searchService;
    private final Scanner scanner;

    public MenuService(SearchService searchService) {
        this.searchService = searchService;
        this.scanner = new Scanner(System.in);
    }

    public void printMenu() {
        System.out.print(MAIN_MENU);
    }

    public void startProcessing() {
        this.printMenu();
        while(true) {
            processMenuInput(scanner.nextLine());
        }
    }

    @VisibleForTesting
    void processMenuInput(String input) {
        switch(input) {
            case SEARCH_OPTION:
                processSearchSelect();
                this.printMenu();
                break;
            case FIELDS_OPTION:
                searchService.printSearchTypes();
                this.printMenu();
                break;
            case QUIT_OPTION:
                System.out.print(QUIT_MESSAGE);
                System.exit(0);
            default:
                System.out.print(INVALID_INPUT_MESSAGE);
        }
    }

    private void processSearchSelect() {
        EntityType entityType = getEntityType();
        String key = getSearchTerm(entityType);
        String value = getSearchValue();

        this.searchService.printSearchResults(entityType, key, value);
    }

    @VisibleForTesting
    EntityType getEntityType() {
        EntityType entityType = null;

        do {
            System.out.print(SELECT_ENTITY_MESSAGE);
            String entityString = scanner.nextLine();

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

    private String getSearchTerm(EntityType entityType) {
        String field = null;

        do {
            System.out.print(SEARCH_TERM_MESSAGE);
            String fieldInput = scanner.nextLine();

            if(this.searchService.isValidField(entityType, fieldInput)) {
                field = fieldInput;
            } else {
                System.out.print(INVALID_INPUT_MESSAGE);
            }
        } while (field == null);

        return field;
    }

    private String getSearchValue() {
        System.out.print(SEARCH_VALUE_MESSAGE);
        return scanner.nextLine();
    }

}
