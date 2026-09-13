function IdeaCard({ idea, onSelect }) {
    return (
        <div
            style={{
                background: "#ffffff",
                borderRadius: "14px",
                padding: "22px",
                marginBottom: "14px",
                border: idea.selected
                    ? "2px solid #111827"
                    : "1px solid #e5e7eb",
                boxShadow: "0 4px 16px rgba(0,0,0,0.05)",
            }}
        >
            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-start",
                    gap: "15px",
                }}
            > <div>
                <h3
                    style={{
                        margin: "0 0 10px",
                        color: "#111827",
                        fontSize: "19px",
                    }}
                >
                    {idea.title} </h3>

                <p
                    style={{
                        margin: 0,
                        color: "#6b7280",
                        lineHeight: "1.5",
                    }}
                >
                    {idea.description}
                </p>
            </div>

                {idea.selected && (
                    <span
                        style={{
                            whiteSpace: "nowrap",
                            fontSize: "13px",
                            fontWeight: "bold",
                            color: "#111827",
                        }}
                    >
        ✓ Copied to Clipboard
      </span>
                )}
            </div>

            {!idea.selected && (
                <button
                    onClick={() => onSelect(idea.id)}
                    style={{
                        marginTop: "18px",
                        padding: "9px 15px",
                        borderRadius: "8px",
                        border: "1px solid #d1d5db",
                        background: "#ffffff",
                        color: "#111827",
                        cursor: "pointer",
                        fontWeight: "bold",
                    }}
                >
                    Use This Idea
                </button>
            )}
        </div>

);
}

export default IdeaCard;
