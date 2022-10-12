# Vertical Slice architecture

Next to controlling dependencies, a typical part of your architecture will be the structure of the code in your project.
A well-thought-out code structure can help a great deal in understanding the purpose of a codebase. It will help people
to figure out where they should place their new code and will help to keep the structure consistent.

Inspired by Uncle Bob's
[Screaming Architecture](https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html),
people began structuring their application code using
[Vertical Slice Architecture](https://jimmybogard.com/vertical-slice-architecture/), sometime also referred to as
_Package-by-feature_.

Defining modules in your system for each `feature`, ensure that code for each feature is close together. That improves
cohesion and lowers coupling. It also allows each feature to evolve on its own pace. You could make changes, like moving
to a different architecture in one feature without affecting any other parts of the system. The added modularity of the
system also helps to discover boundaries and eventually split off pieces of the system as microservices when the need
for that arises.

---

## Exercise

Currently, the package structure resembles the architecture of the system. When you look at it, the only thing it says
is "this app has an api, logic, and some persistence", which does not say much about the actual functionality and
domain. It does not "scream out" that this is a system dealing with renting out lego sets and the kind of functionality
it offers to facilitate that.

- Together with your pairing partner `make a sketch` of which domain specific features this application has.
- Bonus: once you have a sketch, try to `re-organize the packages` so that the codebase "screams out" what functionality
  is in the system and where to find that.
