graphql

dockerize the thing

## June 21 update

Reviving the project. It will be a tool that logs my progress with words.

We'll see how far it gets.

Ok so, I send the word or array of words into the program. It checks if there are duplicates, and adds the non-duped words to the db with a date.

Later, I can retrieve a group of words, perhaps by date_added or by days_since_added, and check my progress with them by
responding to a quiz or something. Perhaps five words at a time in a UI.

I enter my response and, the responses go to the server. The server sends the responses with a prompt to GPT.

GPT sends back a rating per word describing: How well did the description match the meaning?

GPT rates the description out of 5 or 10. GPT also explains the rating.

Was the description not fully encompassing? Was the description lacking in some way? Was one definition not covered at all?

After a month of using the program, the MISSION, the OBJECTIVE is to allow me to
query the program and check: Did I actually learn the words I added? How well did I learn the words?

### Measuring progress

Per word, I can track quiz responses and marks for the quiz as given by GPT.

Say you have a word show up in a quiz 8x over the course of a month. You answer and get marks 4, 4, 5, 8, 8, 8, 8, 8

Then clearly, you learned the word!

You could also submit a word as being "completely new" or "I kind of know this" but let's KISS for now.

### Oh yeah I should do it by hand first

I can do this with a spreadsheet and GPT on my own. I should do it by hand to see if I like doing it.
