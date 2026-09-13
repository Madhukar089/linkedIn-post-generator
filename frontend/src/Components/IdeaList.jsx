import IdeaCard from "./IdeaCard";

function IdeaList({ ideas, onSelect }) {
    if (ideas.length === 0) {
        return (
            <div
                style={{
                    textAlign: "center",
                    marginTop: "35px",
                    color: "#9ca3af",
                }}
            >
                <div style={{ fontSize: "40px" }}>💡</div> <p>Your generated post ideas will appear here.</p> </div>
        );
    }

    return (
        <div style={{ marginTop: "30px" }}>
            <h2
                style={{
                    color: "#111827",
                    marginBottom: "16px",
                }}
            >
                Your Post Ideas </h2>
            {ideas.map((idea) => (
                <IdeaCard
                    key={idea.id}
                    idea={idea}
                    onSelect={onSelect}
                />
            ))}
        </div>

);
}

export default IdeaList;
