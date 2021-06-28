# Zendesk Search - Oliver Reid

### Foreword
Firstly thanks for the opportunity to tackle this coding challenge. It was an enjoyable challenge which included 
a number of implementation decisions and trade-offs. I look forward to discussing my solution with you further 
and receiving your feedback.

### Running the project
You should be able to run this project on your machine using the included jar file `zendesk-search.jar`. Navigate
to the project directory and run `java -jar zendesk-search.jar`. Alternatively, this has been set up as a gradle
project, so you should be able to open the project with an IDE like IntelliJ and run the project from the 
`ZendeskSearch` class (this holds the main method). In addition, significant test converge has been included, so 
you should also be able to run these tests.

Please let me know if you have any issues running this project and I'll be more than happy to assist.

### Structure and Documentation
I have structured my project like so:
* `domain` - holds enums and constants (for things like strings in menus).
* `model` - holds classes which represent the searchable entities and search types.
* `repository` - holds classes which facilitate storing and searching entities.
* `services` - holds services for the user to interact with and search entities/list search types.

Additionally, test coverage for these files is included in the `test` folder whose layout matches the `main` 
implementation.

I have also included a non-formal UML diagram to outline the overall project structure. This is located in `docs/uml.pdf`.

### Implementation
#### Models
* `AbstractEntity` - Abstract class to represent a searchable entity. Holds entity data, type and related entities.
* `Organiztion` - Concrete representation of an organization entity.
* `User` - Concrete representation of a user entity.
* `Ticket` - Concrete representation of a ticket entity.
* `SearchType` - Class to represent a searchable entity. This is used to display details about the fields which can be
searched for an entity on the menu.
  
#### Repository
* `EntityDatstore` - Interface to represent a datastore which stores entities. Other stores would be able to implement
this interface so that `Datstore` could be replaced if the underlying method of storing the entities was changed.
* `Datastore` - Concrete implementation of `EntityDatstore` using a map in form `{ type: { key: { value: [entities] } } }`

#### Services
I tried to move as much of the implementation details into these services to keep the main app as simple as possible. 
These services should be able to operate separately from the main app (be replaceable or dropped into other apps).

* `MenuService` - Provides methods to inform user of menu choices and control input. Calls `SearchService` to facilitate
search and getting information about searchable entities.
* `SearchService` - Provides methods to search datastore and report on searchable entities.

#### Main

* `ZendeskSearch` - Main app which loads JSON data, constructs entities and searchable types, loads them into respective
services and calls `MenuService` to handle user input.
  
### Tradeoffs/Assumptions/Weaknesses

#### Lists vs Maps
One of the first assumptions I made was that something like a Map was preferred to something like a List or lists of List.
This was because search needed to perform well as additional data was added and search times should not grow linearly.
Consequently, I opted to store my data as a multi layered map in the following form `Map<EntityType, Map<String, 
Map<String, List<AbstractEntity>>>>`. In order, the key value pairs represent <type of entity, map>, <field name in entity, map>,
<field value in entity, list of matching entities>. For example the map might look like `{TYPE: { FIELD_NAME: 
{FIELD_VALUE: [MATCHING_ENTITY}}}`. This approach let me do a simple lookup in the map when searching for data, however
came at the cost of being slightly more difficult to implement and having less flexibility than a simple list approach
(i.e. requirements change). Additionally, it made adding data to the store more complex/slower, however lookup much 
quicker. In, a list approach I'd need to filter through the list when performing a search which would scale poorly and 
slow down as the size of the data grew. I believe that the increase in search speed far outweighed the negligibly 
slower add speed, especially considering search is a much more likely operation that addition.

#### Every entity had the same field
I setup my SearchTypes (the things which hold the fields which can be searched on for an entity) to be constructed from an
entity. This means that a SearchType only represents the given entity which was used to construct the SearchType. This
could be a problem since it is possible that not all entities of a given type have the same fields. Therefore, if you 
used an entity which didn't have all possible fields on it to construct the SearchType, the menu option which lists the
fields you can search on for an entity would not list all fields. This could be refactored to ensure that all fields 
are accounted for and instead of constructing a SearchField from an entity, I could have iterated across all entities 
for a given type and created a set of unique search fields, thus ensuring all fields were always included.

#### Flat hierarchy
Originally I had planned on laying out the entities in a hierarchy with organization as the root element and user and
ticket as child elements. This was because user and ticket both had a reference (organization_id) to organization, and I
needed a way to return users and tickets when printing out a matching organization. I would have implemented this by 
adding a list of users and tickets to organization. I decided against doing this since I wasn't sure if this was 
actually a correct representation of how these objects related. Further, it meant that I would be tied to this specific
relationship which made the searchable entities less flexible. For example, if a requirement came that a user had a 
reference to a ticket then I'd need to update the user entity to hold a list of tickets. This could quickly become 
messy. Instead, I treated all entities at the same level in the hierarchy and linked them via a simple list. This meant
that any entity could link to any other entity and there was no limitation.

#### File correctness
I generally trusted the JSON data included. This was mainly to reduce the amount of extra error handling I'd need to
write and keep my solution relatively succinct. In a production environment less trust would be placed in the supplied
data and better should be put in place.