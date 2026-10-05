import { useState, useEffect } from "react";
import { useDebounce } from "./hooks/useDebounce";
import SearchBar from "./components/SearchBar";
import StatusFilter from "./components/StatusFilter";
import TaskTable from "./components/TaskTable";
import { useTasks } from "./hooks/useTasks";

export default function App() {
  const [query, setQuery] = useState("");
  const [status, setStatus] = useState("");
  const [page, setPage] = useState(1);

  const debouncedQuery = useDebounce(query, 400);

  const { tasks, totalPages, loading, error } = useTasks(
    debouncedQuery,
    status,
    page,
    10
  );

  useEffect(() => {
    setPage(1);
  }, [debouncedQuery]);

  const handleStatusChange = (value) => {
    setStatus(value);
    setPage(1);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Task Tracker</h1>
        <p className="subtitle">Internal task management</p>
      </header>

      <div className="controls">
        <SearchBar value={query} onChange={setQuery} />
        <StatusFilter value={status} onChange={handleStatusChange} />
      </div>

      <TaskTable tasks={tasks} loading={loading} error={error} />

      {totalPages > 1 && (
        <div className="pagination">
          <button disabled={page <= 1} onClick={() => setPage((p) => p - 1)}>
            Previous
          </button>
          <span>
            Page {page} of {totalPages}
          </span>
          <button
            disabled={page >= totalPages}
            onClick={() => setPage((p) => p + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}
