const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function generatePostIdeas(topic) {
    const response = await fetch(`${API_BASE_URL}/post-ideas`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ topic }),
    });
    if (!response.ok) {
        throw new Error("Failed to generate post ideas");
    }
    return response.json();
}