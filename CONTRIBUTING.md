## SparkFX

## General Guidelines

If it fixes or improves something, then it will probably be merged.
Try to keep the code style the same as the current project.
Use proper Java naming conventions.

Feel free to create an Issue or submit a Pull Request.


## Detailed Conventions

### Class Names

Always in Pascal case.
  - Acceptable
    - ValueGenerator
    - DatabaseConfiguration
    - LaserFactory
  - Unacceptable
    - someClass
    - reallycoolclass
    - Bestclassever
    
###  Imports
  - No static or wildcard (*) imports unless referencing Spark
  library methods, as this is their intended use (to be statically imported)
  
  - No static import of nested class.
    - Acceptable
      - `import some.package.Logger.Level;`
    - Unacceptable
      - `import static some.package.Logger.*;`
      
  - I may give some liberty if you import the Java Math API or something
  equally as commonplace.

### Ordering of class elements

  - Keep constructors together
  - Keep private fields together
  - Keep public methods together (and with javadoc)
 
## Braces
  
  Braces are not optional, even when they are not explicitly required.
  
  ```
  if (someBoolean)
    someVal++;
  ```  

  The above should not be used. Instead, prefer the below.
  ```
  if (someVal) {
    someVal++;
  }
  ```

  Empty blocks can be written on one line except for try/catch/finally, or any
  time a compound block is used.
  
  `if (caseThatNeverHappens) {}`
  
  This is perfectly fine.
  
## Encoding / Whitespace / Tabs v. Spaces

  UTF-8 until someone can convince me otherwise.
  This is a Java/Groovy project, not Python3.
  
  Indentation is 4 spaces, no tabs.
  I actually don't mind 2 spaces in markup (XML/HTML, etc.)
  
  Keep your Emjois on your smartphone, and out of my repos.
  
## Maximum line length

  Ideally under 80 characters, some of us like to use Vim on occasion.
  100 is pushing it, 120 will probably see me demanding a fix before merging.
  
## Line wrapping
  
  Java can be verbose at times. Wrap where it makes sense.
  
## Vertical whitespace

 ```
 int x = 15;
 Object anObject = getObject(Integer.class, 15);
 anObject.set(x);
 ConnectionFactory.getCustomConnection().updateSomeSetting(anObject, x);
 UI.drawSomething();
 ServerContext.doAnUnrelatedThing();
 ```

The above is ugly, make it pretty.

 ```
 int x = 15;
 Object anObject = getObject(Integer.class, 20);

 anObject.set(x);
 ConnectionFactory.getCustomConnection().updateSomeSetting(anObject, x);

 UI.drawSomething();
 ServerContext.doARelevantThing();
 ```

This last one is much more readable.

## Horizontal whitespace

Spaces between reserved words and the next brace

**Okay:** `if (someThing) {`
**Not Okay:** `if(someThing){`

Err on the side of too much space rather than not enough.

## Horizontal alignment

```
int x;  // this is cool
int aLargeIdentifier // so is this

// The below works also

int x;                  // some comment
int aLargeIdentifier;   // another comment
```

## Enums

A comma and line break after each constant

Do:

```
public enum Things {
    FIRST,
    SECOND,
    THIRD
}
```

I detest this:

```
public enum MessyThings {
  FIRST,SECOND,THIRD;
}
```

### Commas separating parameters

Do:

`someFunction(anInt, another, theLast);`

Do if you hate me:

`someFunction(anInt,another,theLast);`


## Array declarations

Again, this is a Java/Groovy project.
Use Java style array declarations, not C-style.

`new int[] myArray`

Don't do:

`new int myArray[]`

## Switch statements

- Indented just like any  other block
- Line break after each label
- A default statement is required unless you're comparing enums.

## Annotations

One per line

```
@Nullable
@SpecialGetter
@SuppressWarnings("unused")
public String getIt() { ... }
```

## Comments

**Use them**

Block style, single line, either is fine.
Don't leave dangling javadoc.

## Modifiers

When present, these should appear in the following order.
`public protected private abstract default static final transient volatile synchronized native strictfp`

## Numeric literals

Always use uppercase suffix

Do: 
`long myLong = 1000000L`, don't use `long myLong = 1000000l`

## Package names

Concatenated words with no underscores, all lowercase.

## Method names

Camel case

E.g. `myMethod`, or `anEvenBetterMethod`

## Constant names

All uppercase and capitalized

`private static final int PI = 3.141;`

## Non-constant field/parameter/local variable names

Never all-caps.

## Javadoc

Always on `public` or `protected` API, optional on other modifiers.

## Miscellany

- Always use @Override
- Unused exceptions in catch blocks should be called `ignored`
- Always use fully-qualified reference to static method/field
- Don't override `Object.finalize()`