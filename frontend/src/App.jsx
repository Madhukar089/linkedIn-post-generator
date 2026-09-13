import { useState } from "react";
import TopicInput from "./components/TopicInput";
import IdeaList from "./components/IdeaList";
import { generatePostIdeas } from "./api/api";

function App() {
    const [topic, setTopic] = useState("");
    const [ideas, setIdeas] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    function handleTopicChange(value) {
        setTopic(value);
    }

    async function handleGenerate() {
        setLoading(true);
        setError("");
        setIdeas([]);

        try {
            const generatedIdeas = await generatePostIdeas(topic);

            const ideasWithSelectionState = generatedIdeas.map((idea) => ({
                ...idea,
                selected: false,
            }));

            setIdeas(ideasWithSelectionState);
        } catch (error) {
            console.error(error);
            setError("Unable to generate ideas. Please try again.");
        } finally {
            setLoading(false);
        }
    }

    async function handleSelectIdea(id) {
        const selectedIdea = ideas.find((idea) => idea.id === id);

        if (!selectedIdea) {
            return;
        }

        try {
            const textToCopy =
                `${selectedIdea.title}\n\n${selectedIdea.description}`;

            await navigator.clipboard.writeText(textToCopy);

            setIdeas((prevIdeas) =>
                prevIdeas.map((idea) =>
                    idea.id === id
                        ? { ...idea, selected: true }
                        : { ...idea, selected: false }
                )
            );
        } catch (error) {
            console.error("Failed to copy idea:", error);
        }
    }

    return (
        <div style={styles.page}>
            <div style={styles.backgroundGlow} />

            <div style={styles.app}>
                <header style={styles.header}>
                    <div>
                        <h1 style={styles.title}>
                            LinkedIn Idea
                            <span style={styles.titleAccent}> Generator</span>
                        </h1>

                        <p style={styles.subtitle}>
                            Turn what you learned today into your next LinkedIn post.
                        </p>
                    </div>

                    <div style={styles.aiBadge}>
                        <span style={styles.aiDot} />
                        AI Powered
                    </div>
                </header>

                <main style={styles.workspace}>
                    <section style={styles.inputPanel}>
                        <div style={styles.panelLabel}>YOUR INPUT</div>

                        <h2 style={styles.panelTitle}>
                            What did you
                            <br />
                            learn today?
                        </h2>

                        <p style={styles.panelDescription}>
                            Enter a technology, concept, project, or anything you learned.
                            AI will turn it into LinkedIn-ready post ideas.
                        </p>

                        <TopicInput
                            label="Learning topic"
                            placeholder="e.g. React Hooks, Java Streams, AWS..."
                            buttonText={
                                loading ? "Generating..." : "Generate Ideas ✨"
                            }
                            topic={topic}
                            onTopicChange={handleTopicChange}
                            onGenerate={handleGenerate}
                            disabled={loading}
                        />

                        {topic && !loading && (
                            <div style={styles.topicPreview}>
                                <span style={styles.topicDot} />
                                Generating around{" "}
                                <strong>{topic}</strong>
                            </div>
                        )}
                    </section>

                    <section style={styles.resultsPanel}>
                        <div style={styles.resultsHeader}>
                            <div>
                                <div style={styles.panelLabel}>AI OUTPUT</div>

                                <h2 style={styles.resultsTitle}>
                                    Your Post Ideas
                                </h2>
                            </div>

                            {ideas.length > 0 && (
                                <div style={styles.ideaCount}>
                                    {ideas.length} ideas
                                </div>
                            )}
                        </div>

                        {loading && (
                            <div style={styles.loading}>
                                <div style={styles.loadingIcon}>✦</div>

                                <h3 style={styles.loadingTitle}>
                                    Thinking...
                                </h3>

                                <p style={styles.loadingText}>
                                    Creating LinkedIn ideas for you.
                                </p>
                            </div>
                        )}

                        {error && (
                            <div style={styles.error}>
                                {error}
                            </div>
                        )}

                        {!loading && !error && (
                            <IdeaList
                                ideas={ideas}
                                onSelect={handleSelectIdea}
                            />
                        )}
                    </section>
                </main>

                <footer style={styles.footer}>
                    <span>React</span>
                    <span>•</span>
                    <span>Spring Boot</span>
                    <span>•</span>
                    <span>AI Agnostic</span>
                    <span>•</span>
                    <span>30 Days → 30 Builds</span>
                </footer>
            </div>
        </div>
    );
}

const styles = {
    page: {
        minHeight: "100vh",
        background: "#07090d",
        color: "#f5f7fa",
        padding: "32px",
        boxSizing: "border-box",
        fontFamily:
            "Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif",
        position: "relative",
        overflow: "hidden",
    },

    backgroundGlow: {
        position: "fixed",
        width: "500px",
        height: "500px",
        borderRadius: "50%",
        background:
            "radial-gradient(circle, rgba(99,102,241,0.14), transparent 70%)",
        top: "-220px",
        right: "-120px",
        pointerEvents: "none",
    },

    app: {
        maxWidth: "1250px",
        margin: "0 auto",
        position: "relative",
        zIndex: 1,
    },

    header: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "flex-end",
        gap: "20px",
        marginBottom: "28px",
    },

    badge: {
        display: "inline-block",
        color: "#a5b4fc",
        fontSize: "11px",
        fontWeight: "800",
        letterSpacing: "1.5px",
        marginBottom: "12px",
    },

    title: {
        fontSize: "42px",
        lineHeight: "1.05",
        margin: 0,
        letterSpacing: "-1.5px",
        fontWeight: "800",
    },

    titleAccent: {
        color: "#818cf8",
    },

    subtitle: {
        color: "#8b93a3",
        margin: "12px 0 0",
        fontSize: "15px",
    },

    aiBadge: {
        display: "flex",
        alignItems: "center",
        gap: "8px",
        padding: "8px 13px",
        border: "1px solid #242938",
        borderRadius: "999px",
        background: "#0d1017",
        color: "#aeb5c3",
        fontSize: "12px",
        fontWeight: "600",
    },

    aiDot: {
        width: "7px",
        height: "7px",
        borderRadius: "50%",
        background: "#818cf8",
        boxShadow: "0 0 10px rgba(129,140,248,0.8)",
    },

    workspace: {
        display: "grid",
        gridTemplateColumns: "minmax(300px, 0.8fr) minmax(400px, 1.4fr)",
        gap: "20px",
        alignItems: "stretch",
    },

    inputPanel: {
        background: "#0d1016",
        border: "1px solid #202532",
        borderRadius: "20px",
        padding: "30px",
        minHeight: "520px",
        boxSizing: "border-box",
    },

    resultsPanel: {
        background: "#0b0e13",
        border: "1px solid #202532",
        borderRadius: "20px",
        padding: "30px",
        minHeight: "520px",
        boxSizing: "border-box",
    },

    panelLabel: {
        color: "#636b7a",
        fontSize: "10px",
        fontWeight: "800",
        letterSpacing: "1.5px",
        marginBottom: "14px",
    },

    panelTitle: {
        fontSize: "30px",
        lineHeight: "1.15",
        margin: "0 0 14px",
        letterSpacing: "-0.8px",
    },

    panelDescription: {
        color: "#858d9c",
        fontSize: "14px",
        lineHeight: "1.65",
        margin: "0 0 28px",
        maxWidth: "440px",
    },

    topicPreview: {
        marginTop: "20px",
        padding: "12px 14px",
        borderRadius: "10px",
        background: "#11151e",
        border: "1px solid #242938",
        color: "#747d8d",
        fontSize: "12px",
        display: "flex",
        alignItems: "center",
        gap: "8px",
    },

    topicDot: {
        width: "6px",
        height: "6px",
        borderRadius: "50%",
        background: "#818cf8",
    },

    resultsHeader: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "flex-start",
        marginBottom: "10px",
    },

    resultsTitle: {
        fontSize: "25px",
        margin: 0,
        letterSpacing: "-0.5px",
    },

    ideaCount: {
        background: "#151925",
        border: "1px solid #292e3c",
        color: "#929aaa",
        borderRadius: "999px",
        padding: "6px 10px",
        fontSize: "11px",
    },

    loading: {
        minHeight: "380px",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
    },

    loadingIcon: {
        fontSize: "38px",
        color: "#818cf8",
        marginBottom: "14px",
    },

    loadingTitle: {
        margin: 0,
        fontSize: "18px",
    },

    loadingText: {
        color: "#707887",
        fontSize: "13px",
    },

    error: {
        marginTop: "30px",
        padding: "15px",
        borderRadius: "10px",
        background: "#241316",
        border: "1px solid #4b252b",
        color: "#fca5a5",
        fontSize: "13px",
    },

    footer: {
        textAlign: "center",
        color: "#555d6c",
        fontSize: "11px",
        marginTop: "22px",
        display: "flex",
        justifyContent: "center",
        gap: "8px",
    },
};

export default App;