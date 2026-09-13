import { useState } from "react";

function TopicInput({
                        label,
                        placeholder,
                        buttonText,
                        topic,
                        onTopicChange,
                        onGenerate,
                        disabled,
                    }) {
    const [error, setError] = useState("");

    function handleSubmit(event) {
        event.preventDefault();

if (!topic.trim()) {
  setError("Please enter something you learned today.");
  return;
}

setError("");
onGenerate();

    }

    return ( <form onSubmit={handleSubmit}>
        <label
            htmlFor="topic"
            style={{
                display: "block",
                fontWeight: "bold",
                color: "#111827",
                marginBottom: "10px",
            }}
        >
            {label} </label>

        <input
            id="topic"
            type="text"
            value={topic}
            onChange={(event) => onTopicChange(event.target.value)}
            placeholder={placeholder}
            style={{
                width: "100%",
                padding: "14px",
                border: "1px solid #d1d5db",
                borderRadius: "10px",
                fontSize: "16px",
                boxSizing: "border-box",
                outline: "none",
            }}
            disabled={disabled}
        />

        {error && (
            <p
                style={{
                    color: "#dc2626",
                    fontSize: "14px",
                    margin: "8px 0",
                }}
            >
                {error}
            </p>
        )}

        <button
            type="submit"
            style={{
                width: "100%",
                marginTop: "14px",
                padding: "14px",
                border: "none",
                borderRadius: "10px",
                background: "#111827",
                color: "#ffffff",
                fontSize: "16px",
                fontWeight: "bold",
                cursor: "pointer",
            }}
            disabled={disabled}
        >
            {buttonText}
        </button>
    </form>

);
}

export default TopicInput;
