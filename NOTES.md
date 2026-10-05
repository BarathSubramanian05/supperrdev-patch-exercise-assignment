# Notes

## What I changed

The biggest problem was in the search query. `AND` and `OR` weren't grouped, so archived tasks and tasks with the wrong status could slip in through the description match. I replaced the native query with JPA Specifications so the conditions combine properly, and fixed the same parentheses in `db/queries/search_tasks.sql`.

I also moved pagination into the database with `Pageable`. Before, the controller loaded every matching row and sliced a page out in Java. I removed the `Thread.sleep` that made short searches slower than long ones.

On the frontend I updated the API call and hook for the new response shape, fixed the table getting stuck on "Loading..." after an error, reset the page to 1 when the search or status changes (I noticed page 4 staying selected after filtering to a short list), and debounced the search box so it stops firing a request per keystroke.

## What I left alone

- **The Oracle package** has the same AND/OR bug. I can't run it locally, so I didn't want to change code I couldn't test.
- **The double request on page reload** is React StrictMode in dev. It doesn't happen in a production build.
- **Request cancellation** (AbortController). Debounce covers most of the problem, and I wanted to keep the diff small.

## Biggest remaining risk

Input validation. An unknown status value, or `page` / `pageSize` below 1, throws and returns a 500 instead of a clean 400. The unfixed Oracle copy is a close second, since someone could copy that logic into production.

## Tools

I used Claude to help me read the codebase and review my diff, to draft the debouncing hook and to summarize the changes I have made to mention here. I made the Specification and `Pageable` change myself and tested the filters, paging and search in the browser.