# Repo-Local Figma MCP Design

**Goal:** Connect Figma Desktop MCP to this repository only, so Codex agents in this project can read Figma design context without changing global Codex configuration.

## Scope

- Add a project-scoped Codex MCP configuration under `.codex/config.toml`.
- Use the local Figma Desktop MCP server at `http://127.0.0.1:3845/mcp`.
- Keep setup local to this repository.
- Document how agents should reference Figma links in prompts.

## Approach

Codex supports project-scoped MCP configuration via `.codex/config.toml` in trusted projects. This repo defines a single `figma` MCP server that points to the local Figma Desktop MCP endpoint. The Figma desktop app must be running with Dev Mode's desktop MCP server enabled before agents can use it.

## Files

- Create `.codex/config.toml` for the repo-local MCP server definition.
- Update `AGENTS.md` with short usage guidance for Figma links in this repository.
- Create a short implementation plan document for future maintenance.

## Risks

- The Figma desktop app must be installed and running on the same machine as Codex.
- Dev Mode's desktop MCP server must be enabled in Figma before Codex can connect.
- The project must be treated as trusted by Codex for `.codex/config.toml` to apply.
- Without a Figma frame or node link in the prompt, agents will not know which design to read.
