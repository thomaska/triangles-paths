# Minimum path of a Triangle
This project is calculating the minimum path for a Triangle.
For the solution of the exercise, a purely functional approach was taken.

Some basic input validation is performed when the application starts, and then the minimum path is
being calculated. This is being done by starting from the bottom of the triangle and moving up.
In this way we combine every node of the triangle with the 2 paths that are below it and always keep the one with 
the minimum cost. In the end we end up with one path which is the minimum path of the whole triangle.

