import { useState, useEffect } from 'react';
import { fetchTasks } from '../api';

export function useTasks(query, status, page, pageSize) {
  const [tasks, setTasks] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    setError(null)
    fetchTasks({ query, status, page, pageSize })
      .then((data) => {
        setTasks(data.content);
        setTotalPages(data.totalPages);
        setLoading(false);
        console.log(data);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false)
      });
  }, [query, status, page, pageSize]);

  return { tasks, totalPages, loading, error };
}